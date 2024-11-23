package com.example.backend.controller

import com.example.backend.exceptions.AuthenticationException
import com.example.backend.exceptions.PasswordDoesNotMatchException
import com.example.backend.exceptions.RegistrationException
import com.example.backend.exceptions.UserAlreadyExistException
import com.example.backend.registration.*
import com.example.backend.service.AuthenticationService
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import mu.KotlinLogging

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("api/v1/registration")
@Transactional
class RegistrationController(
    private val authenticationService: AuthenticationService,
    @Value("\${frontend.url}") private val frontendUrl: String
) {
        private val log = KotlinLogging.logger {}


        @PostMapping("/register")
        fun register(@RequestBody @Valid request: RegistrationRequest): ResponseEntity<ApiResponse<AuthenticationResponse>> {
            val response = authenticationService.registerNewUser(request)
            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "User registered successfully",
                    data = response
                )
            )
        }

        @PostMapping("/login")
        fun login(@RequestBody @Valid request: AuthenticationRequest): ResponseEntity<ApiResponse<AuthenticationResponse>> {
            val response = authenticationService.authenticateUser(request)
            return ResponseEntity.ok(
                ApiResponse(
                    success = true,
                    message = "Login successful",
                    data = response
                )
            )
        }
}