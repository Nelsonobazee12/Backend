package com.example.backend.service

import com.example.backend.configuration.auth0.Auth0Config
import com.example.backend.registration.Auth0Exception
import com.example.backend.registration.RegistrationRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.postForEntity
import org.springframework.web.util.UriComponentsBuilder

@Service
class Auth0Client(
    private val restTemplate: RestTemplate,
    private val auth0Config: Auth0Config
) {
    companion object {
        private const val AUTH0_CONNECTION = "Username-Password-Authentication"
        private val logger = LoggerFactory.getLogger(Auth0Client::class.java)
    }

    fun findUserByEmail(email: String?): String? {
        if (email == null) return null

        val managementToken = getManagementToken()
        // Create URL with query parameter
        val url = UriComponentsBuilder
            .fromHttpUrl("https://${auth0Config.domain}/api/v2/users-by-email")
            .queryParam("email", email)
            .encode()
            .toUriString()

        val response = try {
            restTemplate.exchange<List<*>>(
                url,
                HttpMethod.GET,
                HttpEntity<Any>(createAuthHeader(managementToken))
            )
        } catch (ex: HttpClientErrorException) {
            logger.error("Error finding user in Auth0: ${ex.responseBodyAsString}")
            throw Auth0Exception("Failed to find user in Auth0: ${ex.message}")
        }

        return response.body
            ?.firstOrNull()
            ?.let { it as? Map<String, Any> }
            ?.get("user_id") as? String
    }


    fun createUser(registration: RegistrationRequest): String {
        val managementToken = getManagementToken()
        val payload = mapOf(
            "email" to registration.email,
            "password" to registration.password,
            "connection" to AUTH0_CONNECTION,
            "name" to registration.name
        )

        val response = try {
            restTemplate.postForEntity<Map<String, Any>>(
                "https://${auth0Config.domain}/api/v2/users",
                HttpEntity(payload, createAuthHeader(managementToken))
            )
        } catch (ex: HttpClientErrorException) {
            logger.error("Failed to create user in Auth0: ${ex.responseBodyAsString}")
            throw Auth0Exception("Failed to create user in Auth0: ${ex.message}")
        }

        return when {
            response.statusCode.is2xxSuccessful ->
                response.body?.get("user_id") as? String
                    ?: throw Auth0Exception("Failed to retrieve Auth0 user ID")
            else -> throw Auth0Exception("Failed to create user in Auth0: ${response.body}")
        }
    }

    fun enableMfa(auth0UserId: String): Boolean {
        val response = try {
            restTemplate.postForEntity<String>(
                "https://${auth0Config.domain}/api/v2/users/$auth0UserId/mfa/enrollments",
                HttpEntity<Any>(createAuthHeader(getManagementToken()))
            )
        } catch (ex: HttpClientErrorException) {
            logger.error("Failed to enable MFA: ${ex.responseBodyAsString}")
            throw Auth0Exception("Failed to enable MFA: ${ex.message}")
        }
        return response.statusCode.is2xxSuccessful
    }

    private fun getManagementToken(): String {
        val payload = mapOf(
            "grant_type" to "client_credentials",
            "client_id" to auth0Config.clientId,
            "client_secret" to auth0Config.clientSecret,
            "audience" to "https://${auth0Config.domain}/api/v2/"
        )

        val response = try {
            restTemplate.postForEntity<Map<*, *>>(
                "https://${auth0Config.domain}/oauth/token",
                HttpEntity(payload)
            )
        } catch (ex: HttpClientErrorException) {
            logger.error("Failed to get management token: ${ex.responseBodyAsString}")
            throw Auth0Exception("Failed to get management token: ${ex.message}")
        }

        return when {
            response.statusCode.is2xxSuccessful ->
                response.body?.get("access_token") as? String
                    ?: throw Auth0Exception("Failed to retrieve Auth0 management token")
            else -> throw Auth0Exception("Failed to retrieve Auth0 management token: ${response.body}")
        }
    }

    private fun createAuthHeader(token: String) = HttpHeaders().apply {
        set("Authorization", "Bearer $token")
        contentType = MediaType.APPLICATION_JSON
    }

}