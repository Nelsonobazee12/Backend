package com.example.backend.service

import com.example.backend.repository.AppUserRepository
import com.example.backend.users.AppUser
import com.example.backend.users.Role
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("integration")
@Transactional
class AppUserServiceIntegrationTest {

    @Autowired
    private lateinit var appUserService: AppUserService

    @Autowired
    private lateinit var appUserRepository: AppUserRepository

    @Test
    fun `should return all users`() {
        val users = listOf(
            AppUser(1L, "nelson", "nelson@gmail.com", "1234", Role.USER, true),
            AppUser(2L, "john", "john@gmail.com", "1234", Role.USER, true),
            AppUser(3L, "emma", "emma@gmail.com", "1234", Role.USER, true)
        )
        appUserRepository.saveAll(users)

        val result = appUserService.getUsers()
        assertEquals(3, result.size)
        assertEquals(users.sortedBy { it.id }, result.sortedBy { it.id })
    }

    @Test
    fun `should find user by email`() {
        val user = AppUser(1L, "nelson", "nelson@gmail.com", "1234", Role.USER, true)
        appUserRepository.save(user)

        val result = appUserService.findByEmail("nelson@gmail.com")
        assertNotNull(result)
        assertEquals("nelson@gmail.com", result?.email)
    }

    @Test
    fun `should save user`() {
        val user = AppUser(null, "nelson", "nelson@gmail.com", "1234", Role.USER, true)
        val savedUser = appUserService.save(user)

        val foundUser = appUserRepository.findById(savedUser.id!!)
        assertNotNull(foundUser)
        assertEquals("nelson", foundUser.get().name)
    }

    @Test
    fun `should delete user by id`() {
        val user = AppUser(null, "nelson", "nelson@gmail.com", "1234", Role.USER, true)
        val savedUser = appUserRepository.save(user)

        appUserService.deleteUserById(savedUser.id!!)
        val foundUser = appUserRepository.findById(savedUser.id!!)
        assertEquals(false, foundUser.isPresent)
    }
}
