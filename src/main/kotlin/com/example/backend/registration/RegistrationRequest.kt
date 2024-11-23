package com.example.backend.registration
import com.example.backend.Entities.users.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class RegistrationRequest(
    @field:NotNull @field:Email
    val email: String?,

    @field:NotNull @field:Size(min = 8)
    val password: String,

    @field:NotBlank
    val name: String?,

    val role: Role = Role.USER,
    val isTwoFactorAuthEnabled: Boolean = false
)

