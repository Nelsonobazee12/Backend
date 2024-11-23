package com.example.backend.security

import com.example.backend.configuration.JwtAuthConverter
import com.example.backend.configuration.auth0.Auth0Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.slf4j.LoggerFactory
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.web.reactive.function.client.WebClient


@EnableWebSecurity
@Configuration
@EnableMethodSecurity
class SecurityConfig(
    @Value("\${frontend.url}") private val frontendUrl: String,
    private val auth0Config: Auth0Config,
    private val jwtAuthConverter: JwtAuthConverter
) {

    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    companion object {
        private val WHITE_LIST_URL = arrayOf(
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
        )
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        logger.debug("Configuring security filter chain")

        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(*WHITE_LIST_URL).permitAll()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(
                        "/api/v1/registration/**",
                        "/error",
                        "/actuator/**",
                        "/confirmEmail",
                        "/confirm-email",
                        "/api/v1/auth/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }

            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.decoder(jwtDecoder())
                    jwt.jwtAuthenticationConverter(jwtAuthConverter)
                }
            }

            .exceptionHandling { ex ->
                ex.authenticationEntryPoint(AuthEntryPoint())
                ex.accessDeniedHandler(CustomAccessDeniedHandler())
            }

        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val issuer = "https://${auth0Config.domain}/"
        val webClient = WebClient.builder().build()

        val jwkSetUri = "$issuer.well-known/jwks.json"
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri)
            .jwsAlgorithm(SignatureAlgorithm.RS256)
            .build()
            .apply {
                setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer))
            }
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {

            allowedOrigins = listOf(frontendUrl)
            addAllowedHeader("*")
            addAllowedMethod("*")
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder().build()
    }
}

