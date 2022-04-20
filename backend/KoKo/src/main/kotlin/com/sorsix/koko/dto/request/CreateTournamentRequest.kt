package com.sorsix.koko.dto.request

import com.sorsix.koko.domain.enumeration.TournamentType
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime

data class CreateTournamentRequest(
    val tournamentName: String,
    val tournamentCategory: String,
    val tournamentLocation: String,
    val tournamentDescription: String,
    val numberOfParticipants: Int,
    val tournamentType: TournamentType,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val tournamentDate: LocalDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    val tournamentTime: LocalTime
)