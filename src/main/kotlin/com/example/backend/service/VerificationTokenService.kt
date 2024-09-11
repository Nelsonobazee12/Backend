//package com.example.backend.service
//
//import com.example.backend.Entities.users.AppUser
//import com.example.backend.repository.AppUserRepository
//import com.example.backend.repository.VerificationTokenRepository
//import com.example.backend.token.VerificationToken
//import org.springframework.stereotype.Service
//import java.time.LocalDateTime
//import java.util.*
//
//
//@Service
//class VerificationTokenService(
//) {
//
//    fun createAndSaveVerificationToken(user: AppUser, tokenRepository: VerificationTokenRepository): String {
//        val token = UUID.randomUUID().toString()
//        val expirationTime = LocalDateTime.now().plusMinutes(30)
//        val verificationToken = VerificationToken(token, user, expirationTime)
//
//        // Save the token in the repository
//        tokenRepository.save(verificationToken)
//
//        return token
//    }
//
//
//    fun validateTokenAndResetPassword(
//        token: String,
//        newPassword: String,
//        tokenRepository: VerificationTokenRepository,
//        userRepository: AppUserRepository
//    ): String {
//        val verificationToken = tokenRepository.findByToken(token) ?: return "INVALID_TOKEN"
//
//        if (verificationToken.expirationTime.isBefore(LocalDateTime.now())) {
//            return "EXPIRED_TOKEN"
//        }
//
//        val user = verificationToken.user
//        user.password = newPassword // Ensure the password is properly hashed
//        userRepository.save(user)
//
//        return "PASSWORD_UPDATED"
//    }
//
//}


