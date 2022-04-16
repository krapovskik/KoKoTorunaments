package com.sorsix.koko.dto.response

data class MatchResponse(
    val id: Long,
    val winner: Int?,
    val isFinished: Boolean,
    val number: Int?,
    val round: Int?,
    val opponent1: OpponentResponse?,
    val opponent2: OpponentResponse?,
)
