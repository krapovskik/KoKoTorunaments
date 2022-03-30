package com.sorsix.koko.dto.request

data class ActivateAccountRequest(val token: String, val firstName: String, val lastName: String, val password: String)
