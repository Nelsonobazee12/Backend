package com.example.backend.service

import com.example.backend.Entities.users.AppUser
import com.example.backend.Entities.users.Role
import com.example.backend.configuration.jwtConfiguration.JwtService
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.TokenRepository
import com.example.backend.token.Token
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomOAuth2UserService(
    @Lazy private val jwtService: JwtService,
    private val userRepository: AppUserRepository,
    private val authenticationService: AuthenticationService,
    private val tokenRepository: TokenRepository
) : DefaultOAuth2UserService() {

    private val logger: Logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        logger.info("Loading user...")

        val oAuth2User = super.loadUser(userRequest)

        logger.info("OAuth2 user loaded successfully: $oAuth2User")

        val oidcUser = oAuth2User as DefaultOidcUser

        if (isOAuth2Login(userRequest)) {
            try {
                val appUser = createOrUpdateAppUser(oidcUser)

                logger.info("AppUser created/updated: $appUser")

                val token = jwtService.generateToken(appUser)
                logger.info("Generated token: $token")

                authenticationService.saveUserToken(appUser, token)

                logger.info("Token saved")

            } catch (e: Exception) {
                logger.error("Error generating or setting token: ", e)
                throw RuntimeException("Error during login processing", e)
            }
        }

        return oAuth2User
    }

    private fun isOAuth2Login(userRequest: OAuth2UserRequest): Boolean {
        val providerName = userRequest.clientRegistration.clientName
        return providerName == "google"
    }

    fun createOrUpdateAppUser(oAuth2User: DefaultOidcUser): AppUser {
        val email = oAuth2User.getAttribute<String>("email") ?: throw IllegalArgumentException("Email is required")
        val fullName = oAuth2User.getAttribute<String>("name") ?: ""
        val profilePicture = oAuth2User.getAttribute<String>("picture") ?: ""

        val existingUser = userRepository.findByEmail(email)

        val appUser = existingUser?.copy(
            name = fullName,
            profileImage = profilePicture,  // This updates the profile image
            roles = Role.USER,
        )
            ?: AppUser(
                email = email,
                name = fullName,
                profileImage = profilePicture,  // This sets the profile image for new users
                roles = Role.USER,
                password = "",
                enabled = true
            )

        return userRepository.save(appUser)
    }


    fun saveUserToken(appUser: AppUser, token: String) {
        val userToken = Token(token = token, appUser = appUser)
        tokenRepository.save(userToken)
    }

}

