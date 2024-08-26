package com.example.backend.service

import com.example.backend.password.PasswordResetToken
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.PasswordResetTokenRepository
import org.springframework.mail.MailSender
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*


@Service
class ChangePasswordService(
    private val userRepository: AppUserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val mailSender: JavaMailSender,
    private val passwordEncoder: PasswordEncoder,
) {

//    @Transactional
//    fun requestPasswordReset(email: String) {
//        val user = userRepository.findByEmail(email)
//            ?: { IllegalStateException("User not found") }
//
//        // Check if a token already exists for the user
//        val resetToken = passwordResetTokenRepository.findByAppUser(user)?.apply {
//            token = UUID.randomUUID().toString()
//            expiryDate = LocalDateTime.now().plusHours(1)
//        } ?: PasswordResetToken(
//            token = UUID.randomUUID().toString(),
//            user = user,
//            expiryDate = LocalDateTime.now().plusHours(1)
//        )
//
//        passwordResetTokenRepository.save(resetToken)
//
//        // Send email with the token
////        sendPasswordResetEmail(user.email, resetToken.token)
//    }

//
//    private fun sendPasswordResetEmail(email: String, token: String) {
//        val message = SimpleMailMessage().apply {
//            setTo(email)
//            subject = "Password Reset Request"
//            text = "To reset your password, use the following token: $token"
//        }
//        mailSender.send(message)
//    }

//    @Transactional
//    fun resetPassword(token: String, newPassword: String, confirmationPassword: String) {
//        if (newPassword != confirmationPassword) {
//            throw IllegalStateException("Passwords do not match")
//        }
//
//        val resetToken = passwordResetTokenRepository.findByToken(token)
//            .orElseThrow { IllegalStateException("Invalid token") }
//
//        if (resetToken.expiryDate.isBefore(LocalDateTime.now())) {
//            throw IllegalStateException("Token expired")
//        }
//
//        val user = resetToken.appUser.apply {
//            password = passwordEncoder.encode(newPassword)
//        }
//
//        userRepository.save(user)
//        passwordResetTokenRepository.delete(resetToken)
//    }
}
