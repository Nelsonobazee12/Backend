package com.example.backend.service

import com.example.backend.Entities.users.AppUser
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.VerificationTokenRepository
import com.example.backend.token.VerificationToken
import jakarta.mail.MessagingException
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Transactional
@Service
class ChangePasswordService(
    private val userRepository: AppUserRepository,
    private val mailSender: JavaMailSender,
    private val tokenRepository: VerificationTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService
) {

    fun requestPasswordReset(email: String) {
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("User not found")
        val token = createAndSaveVerificationToken(user)
        val url = "http://localhost:3000/authentication/reset-password?token=$token"

        try {
            // Send registration email
            val subject = "Support Team"
            val body = "Dear ${user.name},\n\n You requested to reset your password. Click the link to confirm your email ,\n\nPlease visit $url"
            emailService.sendEmail(user.email!!, subject, body)

        } catch (e: MessagingException) {
            throw RuntimeException("Failed to send password reset email", e)
        }
    }

    private fun createAndSaveVerificationToken(user: AppUser): String {
        val token = UUID.randomUUID().toString()
        val expirationTime = LocalDateTime.now().plusMinutes(10)

        // Use a constructor that matches the expected parameters
        val verificationToken = VerificationToken(
            token = token,
            user = user,
            expirationTime = expirationTime
        )

        tokenRepository.save(verificationToken)
        return token
    }


//    @Throws(MessagingException::class)
//    private fun sendPasswordResetEmail() {
//        // Send registration email
//        val subject = "Support Team"
//        val body = "Dear ${user.name},\n\nThank you for registering at our service. Please visit $appUrl to start using your account."
//        emailService.sendEmail(user.email!!, subject, body)
//    }


    fun validateToken(token: String): String {
        val verificationToken = tokenRepository.findByToken(token) ?: return "INVALID_TOKEN"
        return if (verificationToken.expirationTime.isBefore(LocalDateTime.now())) {
            "EXPIRED_TOKEN"
        } else {
            "VALID"
        }
    }

    fun validateTokenAndResetPassword(token: String, newPassword: String): String {
        val verificationToken = tokenRepository.findByToken(token) ?: return "INVALID_TOKEN"
        if (verificationToken.expirationTime.isBefore(LocalDateTime.now())) {
            return "EXPIRED_TOKEN"
        }
        val user = verificationToken.user
        val updateUser = user.updatePassword(passwordEncoder.encode(newPassword))
        userRepository.save(updateUser)
        return "PASSWORD_UPDATED"
    }
}

