package com.sorsix.koko.dto.response

data class OrganizerRequestResponse(
    val requestId: Long,
    val userFullName: String,
    val title: String,
    val description: String,
)
