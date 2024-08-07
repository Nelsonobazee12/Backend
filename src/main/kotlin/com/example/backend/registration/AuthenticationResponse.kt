package com.example.backend.registration

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationResponse (

    @JsonProperty("access_token")
    val accessToken: String?,
)