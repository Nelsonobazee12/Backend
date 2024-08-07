package com.example.backend.registration


data class AuthenticationRequest(
    var email: String? = null,
    var password: String? = null
)
