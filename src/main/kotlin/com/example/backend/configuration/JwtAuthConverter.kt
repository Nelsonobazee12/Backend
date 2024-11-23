package com.example.backend.configuration

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
class JwtAuthConverter : Converter<Jwt, AbstractAuthenticationToken> {
    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        val authorities = getAuthorities(jwt)
        val principal = JwtAuthenticationToken(jwt, authorities)
        principal.isAuthenticated = true
        return principal
    }

    private fun getAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
        return jwt.claims["scope"]?.toString()
            ?.split(" ")
            ?.map { SimpleGrantedAuthority("SCOPE_$it") }
            ?: emptyList()
    }
}