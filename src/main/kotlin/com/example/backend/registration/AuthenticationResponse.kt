package com.example.backend.registration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

// Updated data class to include idToken
data class AuthenticationResponse(
    val accessToken: String? = null,
    val idToken: String? = null,
    val isTwoFactorAuthEnabled: Boolean,
    val secretImageUrl: String? = null,
)