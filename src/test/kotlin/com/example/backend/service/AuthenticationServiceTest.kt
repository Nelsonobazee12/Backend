//package com.example.backend.service
//
//import org.junit.jupiter.api.Assertions.*
//import kotlin.test.Test
//
//class AuthenticationServiceTest {
//
//    @Test
//    fun testOtpVerification(): Unit {
//        val twoFactorService = TwoFactorAuthenticationService()
//
//        // Generate a new secret
//        val secret = twoFactorService.generateNewSecret()
//
//        // Generate a valid code using Google Authenticator or similar app
//        val validCode = "123456" // Replace with actual valid code
//
//        // Test verification
//        val isValid = twoFactorService.isOtpValid(secret, validCode)
//
//        assertTrue(isValid, "OTP verification should succeed with valid code")
//    }
//}