package com.sorsix.koko.util

import com.sorsix.koko.domain.IndividualMatch
import com.sorsix.koko.domain.Match
import com.sorsix.koko.domain.TeamMatch
import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.IndividualTournamentRepository
import com.sorsix.koko.repository.TeamTournamentRepository
import com.sorsix.koko.repository.view.PlayersInIndividualTournamentRepository
import com.sorsix.koko.repository.view.TeamsInTournamentRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class MapperService(
    val individualTournamentRepository: IndividualTournamentRepository,
    val playersInIndividualTournamentRepository: PlayersInIndividualTournamentRepository,
    val teamTournamentRepository: TeamTournamentRepository,
    val teamsInTournamentRepository: TeamsInTournamentRepository
) {

    fun mapResponseToResponseEntity(response: Response) =
        when (response) {
            is SuccessResponse<*> -> ResponseEntity.ok(response)
            is NotFoundResponse -> ResponseEntity(response, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(response)
        }

    fun mapToMatchResponse(match: Match<*>): MatchResponse {

        val (opponent1, opponent2) = when (match) {
            is IndividualMatch -> {
                listOf(
                    match.player1?.let { player ->
                        OpponentResponse(
                            player.id,
                            "${player.firstName} ${player.lastName}",
                            match.score1
                        )
                    },
                    match.player2?.let { player ->
                        OpponentResponse(
                            player.id,
                            "${player.firstName} ${player.lastName}",
                            match.score2
                        )
                    })
            }
            is TeamMatch -> {
                listOf(
                    match.player1?.let { team -> OpponentResponse(team.id, team.name, match.score1) },
                    match.player2?.let { team -> OpponentResponse(team.id, team.name, match.score2) })
            }
        }

        return MatchResponse(
            match.id,
            match.winner,
            match.isFinished,
            match.number,
            match.round,
            opponent1,
            opponent2
        )
    }

    fun mapTournamentsWithParticipantsNumber(tournament: Tournament): TournamentResponse =
        when (tournament.type) {
            TournamentType.INDIVIDUAL -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                if (tournament.timelineType == TimelineTournamentType.COMING_SOON) {
                    individualTournamentRepository.findAllByTournamentId(tournament.id).map {
                        ParticipantResponse(it.id, "${it.player.firstName} ${it.player.lastName}")
                    }
                } else {
                    playersInIndividualTournamentRepository.findAllByTournamentId(tournament.id).map {
                        ParticipantResponse(it.playerId, "${it.firstName} ${it.lastName}")
                    }
                },
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name,
                tournament.organizer.id,
                tournament.description,
                tournament.location,
                tournament.startingDate,
            )
            TournamentType.TEAM -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                if (tournament.timelineType == TimelineTournamentType.COMING_SOON) {
                    teamTournamentRepository.findAllByTournamentId(tournament.id).map {
                        ParticipantResponse(it.id, it.player.name)
                    }
                } else {
                    teamsInTournamentRepository.findAllByTournamentId(tournament.id).map {
                        ParticipantResponse(it.teamId, it.teamName)
                    }
                },
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name,
                tournament.organizer.id,
                tournament.description,
                tournament.location,
                tournament.startingDate,
            )
        }

    fun mapToProfileTournamentResponse(tournament: Tournament) = ProfileTournamentResponse(
        tournament.id,
        tournament.name,
        tournament.type,
        tournament.timelineType
    )
}
