package com.example.backend.service

import com.example.backend.configuration.jwtConfiguration.JwtService
import com.example.backend.exceptions.UserAlreadyExistException
import com.example.backend.registration.AuthenticationRequest
import com.example.backend.registration.AuthenticationResponse
import com.example.backend.registration.RegistrationRequest
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.TokenRepository
import com.example.backend.token.Token
import com.example.backend.token.TokenType
import com.example.backend.Entities.users.AppUser
import com.example.backend.Entities.users.Role
import com.example.backend.exceptions.AuthenticationException
import com.example.backend.exceptions.RegistrationException
import com.example.backend.utilities.UrlUtility
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Lazy
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class AuthenticationService @Lazy constructor(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: AppUserRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val tokenRepository: TokenRepository,
    private val emailService: EmailService,
    private val notificationService: NotificationService,
    @Qualifier("webApplicationContext") private val publisher: ApplicationEventPublisher,
    private val request: HttpServletRequest
) {


    fun registerNewUser(registration: RegistrationRequest, appUrl: String): AuthenticationResponse {
        // Check if a user with the same email already exists
        val existingUser = registration.email?.let { userRepository.findByEmail(it) }
        if (existingUser != null) {
            throw UserAlreadyExistException("User already exists with this email")
        }

        val user = AppUser(
            name = registration.name,
            email = registration.email,
            password = passwordEncoder.encode(registration.password),
            roles = registration.role,
            enabled = true
        )

        try {
            // Save the new user
            val savedUser = userRepository.save(user)
            val jwtToken = jwtService.generateToken(savedUser)

            saveUserToken(savedUser, jwtToken)

            // Send registration email
            val subject = "Welcome to Our Service!"
            val body = "Dear ${user.name},\n\nThank you for registering at our service. Please visit $appUrl to start using your account."
            emailService.sendEmail(user.email!!, subject, body)

            return AuthenticationResponse(
                accessToken = jwtToken
            )

        } catch (e: Exception) {
            // Handle other exceptions if necessary
            throw RegistrationException("An error occurred while registering the user")
        }
    }



    fun registerNewUser(registration: RegistrationRequest, request: HttpServletRequest): AuthenticationResponse {
        val appUrl = UrlUtility.getApplicationUrl(request)
        return registerNewUser(registration, appUrl)
    }

    fun authenticateUsers(authenticationRequest: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticationRequest.email,
                authenticationRequest.password
            )
        )

        val appUser = authenticationRequest.email?.let { userRepository.findByEmail(it) }
            ?: throw UsernameNotFoundException("Username not found")


        try {
            val jwtToken = jwtService.generateToken(appUser)
            revokeAllUserTokens(appUser)
            saveUserToken(appUser, jwtToken)

            // Send registration email
            val subject = "Login Infor!"
            val body = "Dear ${appUser.name},\n Your Account was accessed, confirmed that it was you. \n else you can quickly change your password"
            emailService.sendEmail(appUser.email!!, subject, body)


            //send notification
            notificationService.createNotification("Login successfull", appUser)

            return AuthenticationResponse(
                accessToken = jwtToken,

            )
        } catch (e: Exception) {
            throw AuthenticationException("An error occurred while authenticating the users")
        }
    }

    fun saveUserToken(appUser: AppUser, jwtToken: String) {
        val token = Token(
            appUser = appUser,
            token = jwtToken,
            tokenType = TokenType.BEARER,
            isExpired = false,
            isRevoked = false
        )
        tokenRepository.save(token)
    }

    private fun revokeAllUserTokens(appUser: AppUser) {
        val validUserTokens = tokenRepository.findAllValidTokenByUser(appUser.id ?: return)

        if (validUserTokens.isNullOrEmpty()) return

        validUserTokens.forEach {
            it.isExpired = true
            it.isRevoked = true
        }
        tokenRepository.saveAll(validUserTokens)
    }


//    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
//        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
//        val refreshToken: String
//        val userEmail: String?
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) return
//
//        refreshToken = authHeader.substring(7)
//        userEmail = jwtService.extractUsername(refreshToken)
//
//        if (userEmail != null) {
//            val user = userRepository.findByEmail(userEmail) ?: return
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                val accessToken = jwtService.generateToken(user)
//                revokeAllUserTokens(user)
//                saveUserToken(user, accessToken)
//
//                val authResponse = AuthenticationResponse(
//                    accessToken = accessToken,
//                    refreshToken = refreshToken
//                )
//                response.outputStream.use { os ->
//                    ObjectMapper().writeValue(os, authResponse)
//                }
//            }
//        }
//    }
}


