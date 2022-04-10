package com.sorsix.koko.dto.response

data class TournamentResponse(
    val tournamentId: Long,
    val tournamentName: String,
    val tournamentCategory: String,
    val tournamentActualParticipants: Int,
    val tournamentNumberOfParticipants: Int,
    val tournamentType: String,
    val tournamentTimelineType: String,
)
