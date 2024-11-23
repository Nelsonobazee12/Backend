package com.example.backend.controller

import com.example.backend.repository.AppUserRepository
import com.example.backend.service.AppUserService
import com.example.backend.Entities.users.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1")
class AppUserController(
    private val appUserService: AppUserService,
    private val appUserRepository: AppUserRepository
) : BaseController() {

    @GetMapping("/get_all_users")
    fun getAllUsers(): List<AppUser> {
        return appUserService.getUsers()
    }

    @GetMapping("/get_current_user")
    fun getCurrentUsers(authentication: Authentication): ResponseEntity<AppUser> {
        val user = getCurrentUser(authentication)
        return ResponseEntity.ok(user)
    }

    @PatchMapping("user_id/{id}")
    fun patchUser(@PathVariable id: Long, @RequestBody updates: Map<String, Any>): AppUser {
        val existingUser = appUserRepository.findById(id).orElseThrow {
            throw RuntimeException("User not found with id: $id")
        }

        updates.forEach { (key, value) ->
            when (key) {
                "name" -> existingUser.name = value as String
                "email" -> {
                    val newEmail = value as String
                    // Check if another user with the same email already exists
                    if (appUserRepository.existsByEmail(newEmail) && existingUser.email != newEmail) {
                        throw RuntimeException("Email already in use: $newEmail")
                    }
                    existingUser.email = newEmail
                }

                else -> throw IllegalArgumentException("Field $key not recognized for update")
            }
        }

        return appUserRepository.save(existingUser)
    }


    @DeleteMapping("/user_id/{id}")
    fun deleteUser(@PathVariable id: Long): String {
        val appUser = appUserService.findUserById(id)
        return if (appUser != null) {
            appUserService.deleteUserById(id)
            "User with ID $id has been deleted"
        } else {
            "User with ID $id does not exist"
        }
    }

    @PatchMapping("/upload/profile-image")
    fun updateProfileImage(
        authentication: Authentication,
        @RequestParam("image") imageFile: MultipartFile
    ): ResponseEntity<AppUser> {
        // Call the updated service method
        val appUser = getCurrentUser(authentication)
        val updatedUser = appUserService.updateProfileImage(appUser, imageFile)
        return ResponseEntity.ok(updatedUser)
    }
}
