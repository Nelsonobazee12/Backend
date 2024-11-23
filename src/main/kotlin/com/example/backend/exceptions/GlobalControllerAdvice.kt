package com.example.backend.exceptions

import com.example.backend.registration.ApiResponse
import mu.KotlinLogging
import org.springframework.data.redis.serializer.SerializationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalControllerAdvice {
    private val log = KotlinLogging.logger {}

//    @ExceptionHandler(UnexpectedRollbackException::class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    fun handleUnexpectedRollbackException(ex: UnexpectedRollbackException): ApiResponse<Nothing> {
//        log.error(ex) { "Transaction rollback occurred" }
//        return ApiResponse.error("An error occurred while processing your request. Please try again.")
//    }

    @ExceptionHandler(UserAlreadyExistException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleUserAlreadyExistException(ex: UserAlreadyExistException): ApiResponse<Nothing> {
        log.warn { "Registration attempt for existing user: ${ex.message}" }
        return ApiResponse.error(ex.message ?: "A user with this email already exists")
    }

    @ExceptionHandler(RegistrationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleRegistrationException(ex: RegistrationException): ApiResponse<Nothing> {
        log.error(ex) { "Registration error" }
        return ApiResponse.error(ex.message ?: "Registration failed")
    }

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleAuthenticationException(ex: AuthenticationException): ApiResponse<Nothing> {
        log.error(ex) { "Authentication error" }
        return ApiResponse.error(ex.message ?: "Authentication failed")
    }

    @ExceptionHandler(TwoFactorSetupException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleTwoFactorSetupException(ex: TwoFactorSetupException): ApiResponse<Nothing> {
        log.error(ex) { "Two-factor authentication setup failed" }
        return ApiResponse.error("Failed to set up two-factor authentication. Please try again without 2FA enabled.")
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse(success = false, message = "Invalid verification code", data = null))
    }

//    @ExceptionHandler(Exception::class)
//    fun handleException(ex: Exception): ResponseEntity<String> {
//        // Log the exception (optional)
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body("{\"error\": \"${ex.message}\"}") // Return a JSON error response
//    }

    @ExceptionHandler(SerializationException::class)
    fun handleSerializationException(e: SerializationException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Serialization error: ${e.message}")
    }

}

class TwoFactorSetupException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

