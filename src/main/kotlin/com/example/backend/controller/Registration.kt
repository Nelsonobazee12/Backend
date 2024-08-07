package com.example.backend.controller

import com.example.backend.registration.AuthenticationRequest
import com.example.backend.registration.AuthenticationResponse
import com.example.backend.registration.RegistrationRequest
import com.example.backend.service.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import jakarta.validation.Valid
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

    @PostMapping("/register")
    fun register(@RequestBody registrationRequest: RegistrationRequest, request: HttpServletRequest): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.registerNewUser(registrationRequest, request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/authenticate")
    fun authenticate(@Valid @RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticateUsers(request))
    }

//    @PostMapping("/refresh-token")
//    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
//        authenticationService.refreshToken(request, response)
//    }
}
