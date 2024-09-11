package com.example.backend.service

import com.example.backend.email.registrationEmail.EmailEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
@Async
class EmailService(
    private val eventPublisher: ApplicationEventPublisher? = null
) {

    fun sendEmail(to: String?, subject: String?, body: String?) {
        val emailEvent = EmailEvent(this, to!!, subject!!, body!!)
        eventPublisher!!.publishEvent(emailEvent)
    }
}
