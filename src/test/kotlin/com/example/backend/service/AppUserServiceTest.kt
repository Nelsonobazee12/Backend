//package com.example.backend.service
//
//import com.example.backend.repository.AppUserRepository
//import com.example.backend.repository.TokenRepository
//import com.example.backend.Entities.users.AppUser
//import com.example.backend.Entities.users.Role
//import org.junit.jupiter.api.Assertions.*
//
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.junit.jupiter.MockitoExtension
//import org.mockito.kotlin.verify
//import org.mockito.kotlin.whenever
//
//
//@ExtendWith(MockitoExtension::class)
//class AppUserServiceTest {
//
//    @Mock
//    private lateinit var tokenRepository: TokenRepository
//
//    @Mock
//    private lateinit var appUserRepository: AppUserRepository
//
//    @InjectMocks
//    private lateinit var appUserService: AppUserService
//
//    @Test
//    fun `should return all users`() {
//        val users = listOf(
//            AppUser(1, "nelson","nelson@gmail.com","1234", Role.USER,true),
//            AppUser(2, "john","john@gmail.com","1234", Role.USER,true),
//            AppUser(3, "emma","emma@gmail.com","1234", Role.USER,true)
//        )
//        whenever(appUserRepository.findAll()).thenReturn(users)
//
//        val result = appUserService.getUsers()
//        assertEquals(users, result)
//    }
//
//    @Test
//    fun `should find user by email`() {
//        val user = AppUser(1, "nelson","nelson@gmail.com","1234", Role.USER,true)
//        whenever(appUserRepository.findByEmail("nelson@gmail.com")).thenReturn(user)
//
//        val result = appUserService.findByEmail("nelson@gmail.com")
//        assertEquals(user, result)
//    }
//
//    @Test
//    fun `should save user`() {
//        val user = AppUser(1, "nelson","nelson@gmail.com","1234", Role.USER,true)
//        whenever(appUserRepository.save(user)).thenReturn(user)
//
//        val result = appUserService.save(user)
//        assertEquals(user, result)
//    }
//
//    @Test
//    fun `should delete user by id`() {
//        val userId = 1L
//        appUserService.deleteUserById(userId)
//        verify(appUserRepository).deleteById(userId)
//    }
//}
