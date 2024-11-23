package com.example.backend.email.registrationEmail

import org.springframework.context.ApplicationEvent

class EmailEvent(source: Any?, val sender: String, val to: String, val subject: String, val body: String) :
    ApplicationEvent(source!!)



