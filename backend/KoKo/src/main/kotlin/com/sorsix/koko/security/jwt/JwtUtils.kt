package com.sorsix.koko.security.jwt

import com.sorsix.koko.domain.AppUser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils {

    @Value("\${jwtSecret}")
    private val jwtSecret: String? = null

    @Value("\${jwtExpirationMs}")
    private val jwtExpirationMs: Int? = null

    fun generateJwtToken(authentication: Authentication): String {

        val userPrincipal = authentication.principal as AppUser

        return Jwts.builder()
            .setSubject(userPrincipal.email)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs!!))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String =
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject

    fun validateJwtToken(token: String) = try {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
        true
    } catch (e: Exception) {
        false
    }
}
