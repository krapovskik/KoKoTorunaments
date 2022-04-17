package com.sorsix.koko.dto.request

import com.sorsix.koko.domain.enumeration.TournamentType

data class EditMatchRequest(
    val tournamentType: TournamentType,
    val matchId: Long,
    val score1: Int?,
    val score2: Int?,
    val isFinished: Boolean,
    val winner: Int?,
)
