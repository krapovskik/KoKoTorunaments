package com.sorsix.koko.api

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.dto.request.ActivateAccountRequest
import com.sorsix.koko.dto.request.LoginRequest
import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.dto.response.JwtResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.security.jwt.JwtUtils
import com.sorsix.koko.service.AppUserService
import com.sorsix.koko.util.MapperService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val appUserService: AppUserService,
    val authenticationManager: AuthenticationManager,
    val jwtUtils: JwtUtils,
    val mapperService: MapperService,
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
    fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<out Response> {
        val result = appUserService.registerUser(registerRequest)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @PostMapping("/activate")
    fun activateAccount(@RequestBody activateAccountRequest: ActivateAccountRequest): ResponseEntity<out Response> {
        val result = appUserService.activateAccount(activateAccountRequest)
        return mapperService.mapResponseToResponseEntity(result)
    }
}
