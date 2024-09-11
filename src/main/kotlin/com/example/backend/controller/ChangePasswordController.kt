package com.example.backend.controller
import com.example.backend.password.ForgotPasswordResetRequest
import com.example.backend.password.PasswordResetRequest
import com.example.backend.password.PasswordResetResponse
import com.example.backend.service.ChangePasswordService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class ChangePasswordController(
    private val changePasswordService: ChangePasswordService
) {

    @PostMapping("/forgot-password")
    fun requestPasswordReset(@RequestBody request: ForgotPasswordResetRequest): ResponseEntity<PasswordResetResponse> {
        return try {
            changePasswordService.requestPasswordReset(request.email)
            ResponseEntity.ok(PasswordResetResponse("Password reset link sent to your email."))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(PasswordResetResponse("Error processing your request."))
        }
    }

    @GetMapping("/reset-password")
    fun showResetPasswordPage(@RequestParam token: String): ResponseEntity<PasswordResetResponse> {
        val validationResult = changePasswordService.validateToken(token)
        val message = when (validationResult) {
            "VALID" -> "Token is valid. You can now reset your password."
            "EXPIRED_TOKEN" -> "Reset link has expired. Please request a new one."
            else -> "Invalid reset link."
        }
        val status = if (validationResult == "VALID") HttpStatus.OK else HttpStatus.BAD_REQUEST
        return ResponseEntity(PasswordResetResponse(message), status)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @RequestBody request: PasswordResetRequest
    ): ResponseEntity<PasswordResetResponse> {
        val result = changePasswordService.validateTokenAndResetPassword(request.token, request.newPassword)
        val response = when (result) {
            "INVALID_TOKEN" -> PasswordResetResponse("Invalid token")
            "EXPIRED_TOKEN" -> PasswordResetResponse("Expired token")
            "PASSWORD_UPDATED" -> PasswordResetResponse("Password updated successfully")
            else -> PasswordResetResponse("An error occurred")
        }
        val status = if (result == "PASSWORD_UPDATED") HttpStatus.OK else HttpStatus.BAD_REQUEST
        return ResponseEntity(response, status)
    }
}
