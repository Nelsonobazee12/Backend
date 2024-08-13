package com.example.backend.service

import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.TokenRepository
import com.example.backend.users.AppUser
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class AppUserService(
    private val appUserRepository: AppUserRepository,
    private val tokenRepository: TokenRepository
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
    @Transactional
    fun deleteUserById(id: Long) {
        appUserRepository.deleteById(id)
    }

    fun findUserById(id: Long): AppUser? {
        return appUserRepository.findById(id).orElse(null)
    }


}
