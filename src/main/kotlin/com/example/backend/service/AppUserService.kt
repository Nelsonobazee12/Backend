package com.example.backend.service

import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.TokenRepository
import com.example.backend.Entities.users.AppUser
import jakarta.transaction.Transactional
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@Transactional
class AppUserService(
    private val appUserRepository: AppUserRepository,
    private val tokenRepository: TokenRepository,
    private val cloudinaryService: CloudinaryService
) {

    /**
     * Retrieves all users from the repository.
     * @return a list of all AppUser instances.
     */
    fun getUsers(): List<AppUser> {
        return appUserRepository.findAll()
    }

    /**
     * Finds a user by their email.
     * @param email the email of the user to find.
     * @return the found AppUser or null if no user with the given email exists.
     */
    fun findByEmail(email: String): AppUser? {
        return appUserRepository.findByEmail(email)
    }

    /**
     * Saves the given user to the repository.
     * @param appUser the user to save.
     * @return the saved AppUser instance.
     */
    fun save(appUser: AppUser): AppUser {
        return appUserRepository.save(appUser)
    }

    /**
     * delete the given user from the repository.
     * @param id of the user to delete.
     * delete AppUser instance.
     */

    fun deleteUserById(id: Long) {
        appUserRepository.deleteById(id)
    }

    fun findUserById(id: Long): AppUser? {
        return appUserRepository.findById(id).orElse(null)
    }

    fun updateProfileImage(userId: Long, imageFile: MultipartFile): AppUser {
        val user = appUserRepository.findById(userId).orElseThrow {
            throw IllegalArgumentException("User not found")
        }

        // Upload image to Cloudinary and get the URL
        val imageUrl = cloudinaryService.uploadImage(imageFile)

        // Update the user's profile image URL
        user.profileImage = imageUrl
        return appUserRepository.save(user)
    }

    fun getCurrentUser(userDetails: UserDetails): AppUser? {
        val username = userDetails.username
        return appUserRepository.findByEmail(username)
    }

}
