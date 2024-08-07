package com.example.backend.configuration.jwtConfiguration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@ConfigurationProperties(prefix = "application.backend.jwt")
@Configuration
data class JwtProperties(
    var secretKey: String = "",
    var expiration: Long = 0,
    var refreshTokenExpiration: Long = 0
)
