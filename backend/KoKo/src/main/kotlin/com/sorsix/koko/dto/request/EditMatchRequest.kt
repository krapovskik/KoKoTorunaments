package com.sorsix.koko.dto.request

data class EditMatchRequest(
    val tournamentId: Long,
    val matchId: Long,
    val score1: Int?,
    val score2: Int?,
    val isFinished: Boolean,
    val winner: Int?,
)
