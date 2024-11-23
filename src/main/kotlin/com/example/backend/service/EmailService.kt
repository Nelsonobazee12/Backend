package com.example.backend.service

import com.example.backend.email.registrationEmail.EmailEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
@Async
class EmailService(
    private val eventPublisher: ApplicationEventPublisher? = null
) {

    fun sendEmail(sender: String, to: String?, subject: String?, body: String?) {
        if (to == null || subject == null || body == null) {
            throw IllegalArgumentException("Recipient, subject, and body must not be null.")
        }

        val emailEvent = EmailEvent(this, sender, to, subject, body)
        eventPublisher?.publishEvent(emailEvent)
    }

}
