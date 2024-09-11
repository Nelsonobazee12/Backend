//package com.example.backend.repository
//
//import com.example.backend.Entities.users.AppUser
//import com.example.backend.password.PasswordResetToken
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.stereotype.Repository
//
//@Repository
//interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken, Long> {
//    fun findByOtpToken(token: String): PasswordResetToken?
//    fun findByAppUser(appUser: AppUser): PasswordResetToken?
//}