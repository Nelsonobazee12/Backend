package com.example.backend.service

import com.example.backend.Entities.users.AppUser
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service


@Service
class NonCriticalOperationsEventHandler(
    private val emailService: EmailService,
    private val bankCardService: BankCardService
) {
    private val log = KotlinLogging.logger {}

    @EventListener
    fun onBankCardCreated(event: BankCardCreatedEvent) {
        sendBankCreatedEmail(event.user)
    }

    @EventListener
    fun onUserRegistered(event: UserRegisteredEvent) {
        createBankCard(event.user)
        sendWelcomeEmail(event.user)
    }

    @EventListener
    fun onUserLogin(event: UserLoginEvent) {
        sendLoginEmail(event.user)
    }

    private fun createBankCard(user: AppUser) {
        try {
            bankCardService.createBankCard(user.id!!, 0.0)
            log.info("Bank card created successfully for user ${user.email}")
        } catch (ex: Exception) {
            log.error("Failed to create bank card for user ${user.email}", ex)
        }
    }

    private fun sendBankCreatedEmail(user: AppUser) {
        val sender = "BankDash <noreply@bankdash.com>"
        val subject = "Credit Card Created!"
        val body = """
            Dear ${user.name},

            A default Credit Card has been created for you.
            You can start adding funds to your card.

            Best regards,
            The BankDash Team
            """.trimIndent()

        emailService.sendEmail(sender, user.email!!, subject, body)
        log.info("Bank card creation email sent to ${user.email}")
    }

    private fun sendWelcomeEmail(user: AppUser) {
        val sender = "BankDash <noreply@bankdash.com>"
        val subject = "Welcome to BankDash!"
        val body = """
            Dear ${user.name},

            Thank you for registering. Please visit to start using your account.

            Best regards,
            The BankDash Team
            """.trimIndent()

        emailService.sendEmail(sender, user.email!!, subject, body)
        log.info("Welcome email sent to ${user.email}")
    }

    private fun sendLoginEmail(user: AppUser) {
        val sender = "BankDash <noreply@bankdash.com>"
        val subject = "Login Infor!"
        val body = """
            Dear ${user.name},
             
            Your Account was accessed, confirmed that it was you. 
            else you can quickly change your password""".trimIndent()


        emailService.sendEmail(sender, user.email!!, subject, body)
        log.info("login email sent to ${user.email}")
    }
}

class BankCardCreatedEvent(val user: AppUser)
class UserRegisteredEvent(val user: AppUser)
class UserLoginEvent(val user: AppUser)
