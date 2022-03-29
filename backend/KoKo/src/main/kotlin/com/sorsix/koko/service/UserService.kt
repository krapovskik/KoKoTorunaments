package com.sorsix.koko.service

import com.sorsix.koko.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}