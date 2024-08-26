package com.example.backend.security

import com.example.backend.configuration.jwtConfiguration.JwtAuthenticationConfiguration
import com.example.backend.service.CustomUserDetailsService
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
@Transactional
class ApplicationSecurityConfig (
    private val jwtAuthenticationConfig: JwtAuthenticationConfiguration,
    private val authenticationProvider: AuthenticationProvider,

    private val logoutHandler: LogoutHandler,
//    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
//    private val customOAuth2UserService: CustomOAuth2UserService,
    @Value("\${frontend.url}") private val frontendUrl: String
) {

    private val logger = LoggerFactory.getLogger(ApplicationSecurityConfig::class.java)

    companion object {
        private val WHITE_LIST_URL = arrayOf(
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
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
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations().toString()).permitAll()
                    .requestMatchers(
                        "/api/v1/registration/**",
                        "/error",
                        "/actuator/**",
                        "/confirmEmail",
                        "/confirm-email",
                        "/reset-password",
                        "/request-password-reset"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
//            .oauth2Login {
//                it
//                    .loginPage("$frontendUrl/login")
//                    .failureUrl("$frontendUrl/login?error")
//                    .permitAll()
//                    .userInfoEndpoint { userInfo ->
//                        userInfo.userService(customOAuth2UserService)
//                    }
//                    .successHandler(oAuth2LoginSuccessHandler)
//            }
            .logout {
                it
                    .logoutUrl("/api/v1/logout")
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                    .logoutSuccessHandler { _, _, _ -> SecurityContextHolder.clearContext() }
            }
//            .exceptionHandling {
//                it.accessDeniedHandler(CustomAccessDeniedHandler("/access-denied"))
//            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationConfig, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
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
}
