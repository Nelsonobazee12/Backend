package com.example.backend.email

data class EmailRequest (
    val sender: String,
    val subject: String,
    val body: String,
    val recipient: String
)