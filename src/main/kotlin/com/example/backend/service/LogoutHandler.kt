//package com.example.backend.service
//
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.security.core.Authentication
//import org.springframework.security.web.authentication.logout.LogoutHandler
//import org.springframework.stereotype.Service
//
//
//@Service
//class LogoutService(
//    private val tokenRepository: TokenRepository
//) : LogoutHandler {
//
//    override fun logout(
//        request: HttpServletRequest,
//        response: HttpServletResponse,
//        authentication: Authentication?
//    ) {
//        val authHeader = request.getHeader("Authorization")
//        val jwt: String?
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return
//        }
//
//        jwt = authHeader.substring(7)
//        val storedToken = tokenRepository.findByToken(jwt)
//            ?: throw IllegalArgumentException("Token not found")
//
//        storedToken.apply {
//            isExpired = true
//            isRevoked = true
//            tokenRepository.save(this)
//        }
//    }
//}
