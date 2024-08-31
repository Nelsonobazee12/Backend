package com.example.backend.security

import com.example.backend.configuration.jwtConfiguration.JwtService
import com.example.backend.service.AuthenticationService
import com.example.backend.service.CustomOAuth2UserService
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.query.sqm.tree.SqmNode.log
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class OAuth2LoginSuccessHandler(
    @Lazy private val jwtService: JwtService,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val authenticationService: AuthenticationService,
//    private val publisher: ApplicationEventPublisher
) : SavedRequestAwareAuthenticationSuccessHandler() {

    @Value("\${frontend.url}")
    private lateinit var frontendUrl: String

    private val logger: Logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        try {
            val oidcUser = authentication.principal as DefaultOidcUser
            val appUser = customOAuth2UserService.createOrUpdateAppUser(oidcUser)
            val jwtToken = jwtService.generateToken(appUser)
            authenticationService.saveUserToken(appUser, jwtToken)
            log.info("Generated JWT token: $jwtToken")

            val redirectUrl = "$frontendUrl/authentication/oauth2-callback?access_token=$jwtToken"
            log.info("Redirecting to URL: $redirectUrl")
            response.sendRedirect(redirectUrl)

        } catch (e: Exception) {
            log.error("Error generating JWT token: ", e)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating JWT token")
        }
    }
}

