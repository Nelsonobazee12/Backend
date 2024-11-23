package com.example.backend.registration

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class AuthenticationRequest(
    @field:NotNull @field:Email
    val email: String?,

    @field:NotNull
    val password: String
)
