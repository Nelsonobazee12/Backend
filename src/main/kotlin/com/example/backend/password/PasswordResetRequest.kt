package com.example.backend.password

data class PasswordResetRequest (
    val token : String,
    val newPassword: String
)