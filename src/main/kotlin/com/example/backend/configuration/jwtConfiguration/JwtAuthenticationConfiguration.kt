package com.example.backend.configuration.jwtConfiguration

import com.example.backend.repository.TokenRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.context.annotation.Lazy
import java.io.IOException



@Component
class JwtAuthenticationConfiguration @Lazy constructor(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
    private val tokenRepository: TokenRepository
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationConfiguration::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (request.servletPath.contains("/registration")) {
                filterChain.doFilter(request, response)
                return
            }

            val authHeader: String? = request.getHeader("Authorization")
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)
                return
            }

            val jwt = authHeader.substring(7)
            val userEmail = jwtService.extractUsername(jwt)

            if (userEmail.isNotBlank() && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(userEmail)
                val isTokenValid = tokenRepository.findByToken(jwt)
                    ?.let { !it.isExpired && !it.isRevoked }
                    ?: false

                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (e: ExpiredJwtException) {
            log.error("Expired JWT token", e)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("JWT token has expired. Please log in again.")
        } catch (e: UnsupportedJwtException) {
            log.error("Invalid JWT token", e)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Invalid JWT token. Please provide a valid token.")
        } catch (e: SignatureException) {
            log.error("Invalid JWT token", e)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Invalid JWT token. Please provide a valid token.")
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token", e)
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Invalid JWT token. Please provide a valid token.")
        } catch (e: Exception) {
            log.error("Unexpected error occurred", e)
            response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            response.writer.write("An unexpected error occurred. Please try again later.")
        }

        filterChain.doFilter(request, response)
    }
}
