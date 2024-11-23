package com.example.backend.email.registrationEmail

import org.springframework.context.event.EventListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class EmailEventListener(private val mailSender: JavaMailSender) {

    @EventListener
    fun handleEmailEvent(event: EmailEvent) {
        val message = SimpleMailMessage()
        message.from = event.sender
        message.setTo(event.to)
        message.subject = event.subject
        message.text = event.body
        mailSender.send(message)
    }
}

