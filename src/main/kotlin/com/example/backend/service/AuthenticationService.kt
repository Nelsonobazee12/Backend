package com.example.backend.service
import com.example.backend.Entities.users.AppUser
import com.example.backend.bankAccount.BankCard
import com.example.backend.configuration.auth0.Auth0Config
import com.example.backend.exceptions.*
import com.example.backend.registration.*
import com.example.backend.registration.Auth0TokenResponse
import com.example.backend.repository.AppUserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity


@Service
class AuthenticationService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: AppUserRepository,
    private val auth0Client: Auth0Client,
    private val auth0Config: Auth0Config,
    private val bankCardService: BankCardService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val notificationService: NotificationService,
    @Qualifier("restTemplate") private val restTemplate: RestTemplate
) {

    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Transactional
    fun registerNewUser(registration: RegistrationRequest): AuthenticationResponse {
        validateNewUser(registration)
        val auth0UserId = registerAuth0User(registration)
        val savedUser = saveLocalUser(registration, auth0UserId)

        // create bank card and sending email
        applicationEventPublisher.publishEvent(UserRegisteredEvent(savedUser))

        // Notify the user about the successful registration
        notificationService.createNotification("User Created successfully", savedUser)

        return createAuthenticationResponse(savedUser)
    }


    fun authenticateUser(request: AuthenticationRequest): AuthenticationResponse {
        // Validate input
        val email = request.email ?: throw BadCredentialsException("Email is required")
        val password = request.password ?: throw BadCredentialsException("Password is required")

        // Authenticate with Auth0 to get tokens
        val tokenResponse = try {
            authenticateWithAuth0(email, password)
        } catch (e: Exception) {
            logger.error(e) { "Authentication failed" }
            throw BadCredentialsException("Invalid credentials")
        }

        // Fetch local user
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")

        // Notify and send email
        applicationEventPublisher.publishEvent(UserLoginEvent(user))

        // Notify the user about the successful creation
        notificationService.createNotification("Login successfully", user)

        return AuthenticationResponse(
            accessToken = tokenResponse.accessToken,
            idToken = tokenResponse.idToken,
            isTwoFactorAuthEnabled = user.isTwoFactorAuthEnabled,
            secretImageUrl = null
        )
    }

    private fun authenticateWithAuth0(email: String, password: String): Auth0TokenResponse {
        val headers = org.springframework.http.HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val payload = mapOf(
            "grant_type" to "password",
            "client_id" to auth0Config.clientId,
            "client_secret" to auth0Config.clientSecret,
            "audience" to auth0Config.audience,
            "username" to email,
            "password" to password,
            "scope" to "openid profile email",
//            "connection" to "Username-Password-Authentication"
        )

        val request = HttpEntity(payload, headers)

        val response = try {
            restTemplate.postForEntity<Map<String, Any>>(
                "https://${auth0Config.domain}/oauth/token",
                request
            )
        } catch (ex: HttpClientErrorException) {
            logger.error(ex) { "Auth0 authentication failed: ${ex.responseBodyAsString}" }
            throw AuthenticationException("Authentication failed")
        }

        return when {
            response.statusCode.is2xxSuccessful -> {
                val body = response.body ?: throw AuthenticationException("Empty response")
                Auth0TokenResponse(
                    accessToken = body["access_token"] as? String,
                    idToken = body["id_token"] as? String
                )
            }
            else -> throw AuthenticationException("Authentication failed")
        }
    }


    private fun validateNewUser(registration: RegistrationRequest) {
        registration.email?.let { email ->
            userRepository.findByEmail(email)?.let {
                throw UserAlreadyExistException("User already exists with email: $email")
            }
        }
    }

    private fun registerAuth0User(registration: RegistrationRequest): String {
        val existingAuth0User = auth0Client.findUserByEmail(registration.email)
        if (existingAuth0User != null) {
            return existingAuth0User
        }
        return auth0Client.createUser(registration)
    }

    private fun saveLocalUser(registration: RegistrationRequest, auth0UserId: String): AppUser {
        val user = AppUser(
            name = registration.name,
            email = registration.email,
            password = passwordEncoder.encode(registration.password),
            roles = registration.role,
            auth0Id = auth0UserId,
            isTwoFactorAuthEnabled = registration.isTwoFactorAuthEnabled,
            enabled = true
        )
        return userRepository.save(user)
    }

    private fun createAuthenticationResponse(user: AppUser): AuthenticationResponse {
        return AuthenticationResponse(
            accessToken = null,
            idToken = null,
            isTwoFactorAuthEnabled = user.isTwoFactorAuthEnabled
        )
    }
}

