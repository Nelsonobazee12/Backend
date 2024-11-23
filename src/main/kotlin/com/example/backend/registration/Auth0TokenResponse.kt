package com.example.backend.registration


// Updated data class for Auth0 token response
data class Auth0TokenResponse(
    val accessToken: String?,
    val idToken: String?
)