package com.example.backend.security

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class AuthUtils {
    fun getCurrentUserId(authentication: Authentication): String {
        val jwt = authentication.credentials as Jwt
        return jwt.subject
    }

    fun getCurrentUserEmail(authentication: Authentication): String? {
        val jwt = authentication.credentials as Jwt
        return jwt.claims["email"] as? String
    }

    fun hasScope(authentication: Authentication, scope: String): Boolean {
        return authentication.authorities.any { it.authority == "SCOPE_$scope" }
    }
}
