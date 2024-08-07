package com.example.backend.registration
import com.example.backend.users.Role

data class RegistrationRequest(
    val name: String?,
    val email: String?,
    val password: String?,
    val role: Role = Role.USER
)

