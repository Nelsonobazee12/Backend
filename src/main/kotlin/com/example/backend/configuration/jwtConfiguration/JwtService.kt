package com.example.backend.configuration.jwtConfiguration

import com.example.backend.repository.AppUserRepository
import org.springframework.stereotype.Service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    private val jwtProperties: JwtProperties,
    private val userRepository: AppUserRepository,
) {

    fun extractUsername(token: String): String {
        return extractClaims(token, Claims::getSubject)
    }

    fun <T> extractClaims(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw UsernameNotFoundException("User not found: ${userDetails.username}")
        return generateToken(
            mutableMapOf(), userDetails, user.id ?: throw IllegalArgumentException("User ID cannot be null"), user.roles.toString()
        )
    }

    fun generateToken(
        extraClaims: MutableMap<String, Any>,
        userDetails: UserDetails,
        userId: Long,
        role: String
    ): String {
        extraClaims["sub"] = userDetails.username
        extraClaims["id"] = userId
        extraClaims["role"] = role

        return buildToken(extraClaims, userDetails, jwtProperties.expiration)
    }

//    fun generateRefreshToken(userDetails: UserDetails): String {
//        return buildToken(mutableMapOf(), userDetails, jwtProperties.refreshTokenExpiration)
//    }

    private fun buildToken(claims: MutableMap<String, Any>, userDetails: UserDetails, expiration: Long): String {
        val now = System.currentTimeMillis()
        val issuedAt = Date(now)
        val expDate = Date(now + expiration)
        println("Issued at (iat): ${issuedAt.time / 1000}")
        println("Expiration (exp): ${expDate.time / 1000}")

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.username)
            .setIssuedAt(issuedAt)
            .setExpiration(expDate)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaims(token, Claims::getExpiration)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes = Base64.getDecoder().decode(jwtProperties.secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

//    fun getRefreshTokenExpiration(): Long {
//        return jwtProperties.refreshTokenExpiration
//    }
}
