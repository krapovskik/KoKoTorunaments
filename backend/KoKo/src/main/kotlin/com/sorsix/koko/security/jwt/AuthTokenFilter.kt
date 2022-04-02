package com.sorsix.koko.security.jwt

import com.sorsix.koko.service.AppUserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthTokenFilter(val jwtUtils: JwtUtils, val appUserService: AppUserService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request)
            jwt?.let {
                if (jwtUtils.validateJwtToken(it)) {
                    val username: String = jwtUtils.getUsernameFromJwtToken(it)
                    val userDetails = appUserService.loadUserByUsername(username)
                    userDetails?.let {
                        val authentication = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }
        } catch (_: Exception) {

        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization")

        if (!authHeader.isNullOrEmpty() && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7)
        }

        return null
    }
}
