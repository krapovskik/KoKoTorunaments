package com.sorsix.koko.dto.response

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType

data class ProfileTournamentResponse(
    val tournamentId: Long,
    val tournamentName: String,
    val tournamentType: TournamentType,
    val timelineTournamentType: TimelineTournamentType
)