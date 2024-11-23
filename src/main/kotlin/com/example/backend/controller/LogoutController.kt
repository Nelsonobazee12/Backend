//package com.example.backend.controller
//
//import com.example.backend.service.LogoutService
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RestController
//
//
//@RestController("/api/v1/logout")
//class LogoutController(
//    private val logoutService: LogoutService
//) {
//
//    @GetMapping()
//    fun logout(request: HttpServletRequest, response: HttpServletResponse) : ResponseEntity<String> {
//        logoutService.logout(request, response, null)
//        return ResponseEntity.ok("Logout success")
//    }
//}