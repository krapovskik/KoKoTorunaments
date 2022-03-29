package com.sorsix.koko.service

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.UserRole
import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = userRepository.findAppUserByEmail(username)

    fun registerUser(registerRequest: RegisterRequest): String {
        val (firstName, lastName, email, password) = registerRequest

        val appUser = AppUser(
            0,
            firstName,
            lastName,
            email,
            passwordEncoder.encode(password),
            UserRole.PLAYER,
            true
        )

        saveUser(appUser)

        return "User registered successfully"
    }

    fun saveUser(appUser: AppUser) = userRepository.save(appUser)
}
