package com.sorsix.koko.api

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.dto.request.ActivateAccountRequest
import com.sorsix.koko.dto.request.LoginRequest
import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.dto.response.ErrorResponse
import com.sorsix.koko.dto.response.JwtResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.security.jwt.JwtUtils
import com.sorsix.koko.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val userService: UserService,
    val authenticationManager: AuthenticationManager,
    val jwtUtils: JwtUtils
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username.lowercase(),
                loginRequest.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)

        val appUser = authentication.principal as AppUser
        return ResponseEntity.ok(
            JwtResponse(
                jwt,
                appUser.id,
                appUser.firstName,
                appUser.lastName,
                appUser.email,
                appUser.appUserRole.name
            )
        )
    }

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<Response> {
        return when(val result = userService.registerUser(registerRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is ErrorResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @PostMapping("/activate")
    fun activateAccount(@RequestBody activateAccountRequest: ActivateAccountRequest): ResponseEntity<Response> {
        return when(val result = userService.activateAccount(activateAccountRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is ErrorResponse -> ResponseEntity.badRequest().body(result)
        }
    }
}
