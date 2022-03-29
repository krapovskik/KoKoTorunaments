package com.sorsix.koko.dto.request

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)
