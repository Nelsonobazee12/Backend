package com.example.backend.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import jakarta.servlet.ServletException
import java.io.IOException

class CustomAccessDeniedHandler : AccessDeniedHandler {
    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.FORBIDDEN.value()
        response.writer.write(
            ObjectMapper().writeValueAsString(mapOf(
                "error" to "Forbidden",
                "message" to (accessDeniedException.message ?: "Access denied"),
                "path" to request.servletPath,
                "status" to HttpStatus.FORBIDDEN.value()
            ))
        )
    }
}
