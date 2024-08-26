package com.example.backend.controller

import com.example.backend.password.PasswordResetRequest
import com.example.backend.service.ChangePasswordService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/")
class ChangePasswordController(
    private val changePasswordService: ChangePasswordService,
) {
//
//    @PostMapping("/change-password-request")
//    fun changePasswordRequest(@RequestBody request: PasswordResetRequest) : ResponseEntity<Any> {
//        changePasswordService.resetPasswordRequest(request.email)
//        return ResponseEntity.ok().build()
//    }

}