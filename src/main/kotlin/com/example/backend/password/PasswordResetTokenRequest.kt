package com.example.backend.password

data class PasswordResetTokenRequest (
    var otpToken: String,
    val newPassword: String? = null,
    val confirmationPassword: String? = null
)