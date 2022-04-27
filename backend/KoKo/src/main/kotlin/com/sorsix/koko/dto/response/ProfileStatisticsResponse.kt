package com.sorsix.koko.dto.response

data class ProfileStatisticsResponse(
    val fullName: String,
    val won: Int,
    val loss: Int,
    val others: Int,
    val profilePhoto: String
)
