package com.sorsix.koko.dto.response

import java.time.LocalDateTime

data class TournamentResponse(
    val tournamentId: Long,
    val tournamentName: String,
    val tournamentCategory: String,
    val tournamentActualParticipants: Int,
    val tournamentNumberOfParticipants: Int,
    val tournamentType: String,
    val tournamentTimelineType: String,
    val organizerId: Long,
    val description: String,
    val location: String,
    val startingDate: LocalDateTime,
)
