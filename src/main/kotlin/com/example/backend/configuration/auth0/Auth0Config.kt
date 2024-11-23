package com.example.backend.configuration.auth0

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ConfigurationProperties(prefix = "auth0")
class Auth0ConfigProperties {
    var domain: String = ""
    var clientId: String = ""
    var clientSecret: String = ""
    var audience: String = ""
}

@Configuration
class AppConfig(
    private val auth0ConfigProperties: Auth0ConfigProperties
) {
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun auth0Config(): Auth0Config = Auth0Config(
        domain = auth0ConfigProperties.domain,
        clientId = auth0ConfigProperties.clientId,
        clientSecret = auth0ConfigProperties.clientSecret,
        audience = auth0ConfigProperties.audience
    )
}

data class Auth0Config(
    val domain: String,
    val clientId: String,
    val clientSecret: String,
    val audience: String
)

