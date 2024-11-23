//package com.example.backend.service
//
//
//
//import com.example.backend.repository.AppUserRepository
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.core.userdetails.UsernameNotFoundException
//import org.springframework.stereotype.Service
//
//@Service
//class CustomUserDetailsService(private val userRepository: AppUserRepository) : UserDetailsService {
//
//    override fun loadUserByUsername(username: String): UserDetails {
//        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("User not found")
//        return user
//    }
//}
