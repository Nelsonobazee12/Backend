package com.example.backend.service

import com.example.backend.repository.AppUserRepository
import com.example.backend.Entities.users.AppUser
import com.example.backend.exceptions.NotFoundException
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
@Transactional
class AppUserService(
    private val appUserRepository: AppUserRepository,
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

//    @CachePut(value = ["users"], key = "#appUser.email")
    fun updateUser(appUser: AppUser): AppUser {
        return appUserRepository.save(appUser)
    }

    @Transactional
//    @CacheEvict(value = ["users"], key = "#userDetails.username")
    fun updateProfileImage(userDetails: UserDetails, imageFile: MultipartFile): AppUser {
        // Cast UserDetails to AppUser directly and ensure it's not null
        val user = userDetails as? AppUser ?: throw NotFoundException("User not found")

        // Upload image to Cloudinary
        val imageUrl = cloudinaryService.uploadImage(imageFile)

        // Update the user's profile image URL
        user.profileImage = imageUrl

        // Save and return the updated user
        return appUserRepository.save(user)
    }

    fun findByAuth0Id(userId: String?): AppUser? {
       return userId?.let { appUserRepository.findByAuth0Id(it) }
    }

}
