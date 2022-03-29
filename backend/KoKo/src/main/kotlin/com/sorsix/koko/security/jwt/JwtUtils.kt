package com.sorsix.koko.security.jwt

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value

class JwtUtils {

    @Value("\${jwtSecret}")
    private val jwtSecret: String? = null

    fun getUsernameFromJwtToken(token: String): String =
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).body.subject

    fun validateJwtToken(token: String) = try {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token)
        true
    } catch (e: Exception) {
        false
    }
}