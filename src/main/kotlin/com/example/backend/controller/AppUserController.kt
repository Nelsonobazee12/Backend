package com.example.backend.controller

import com.example.backend.service.AppUserService
import com.example.backend.users.AppUser
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/users")
class AppUserController(private val appUserService: AppUserService) {


    @GetMapping("/get_all_users")
    fun getAllUsers(): List<AppUser> {
        return appUserService.getUsers()
    }

    @PostMapping
    fun createUser(@RequestBody appUser: AppUser): AppUser {
        return appUserService.save(appUser)
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

}
