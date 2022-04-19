package com.sorsix.koko.service

import com.sorsix.koko.domain.*
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.request.CreateTournamentRequest
import com.sorsix.koko.dto.request.EditMatchRequest
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.*
import com.sorsix.koko.repository.view.PlayersInIndividualTournamentRepository
import com.sorsix.koko.repository.view.TeamsInTournamentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

@Service
class TournamentService(
    val tournamentRepository: TournamentRepository,
    val playersInIndividualTournamentRepository: PlayersInIndividualTournamentRepository,
    val teamsInTournamentRepository: TeamsInTournamentRepository,
    val teamTournamentRepository: TeamTournamentRepository,
    val appUserTournamentRepository: AppUserTournamentRepository,
    val teamService: TeamService,
    val appUserService: AppUserService,
    val teamMatchRepository: TeamMatchRepository,
    val teamMatchesTournamentRepository: TeamMatchesTournamentRepository,
    val individualMatchRepository: IndividualMatchRepository,
    val individualMatchesTournamentRepository: IndividualMatchesTournamentRepository
) {

    fun findAll(tournamentId: Long): List<Tournament> = tournamentRepository.findAll()

    fun createTournament(request: CreateTournamentRequest): Response {
        val dateTime = LocalDateTime.of(request.tournamentDate, request.tournamentTime)
        tournamentRepository.save(
            Tournament(
                name = request.tournamentName,
                category = request.tournamentCategory,
                numberOfParticipants = request.numberOfParticipants,
                type = request.tournamentType,
                timelineType = TimelineTournamentType.COMING_SOON,
                location = request.tournamentLocation,
                description = request.tournamentDescription,
                startingDate = dateTime,
                organizer = SecurityContextHolder.getContext().authentication.principal as AppUser
            )
        )
        return SuccessResponse("Successfully create tournament.")
    }


    @Transactional
    fun updateTournament(
        tournamentId: Long,
        tournamentName: String,
        tournamentCategory: String,
        numberOfParticipants: Int,
        tournamentType: TournamentType,
        tournamentTimelineType: TimelineTournamentType
    ): Response =
        if (tournamentRepository.updateTournament(
                tournamentId,
                tournamentName,
                tournamentCategory,
                numberOfParticipants,
                tournamentType,
                tournamentTimelineType
            ) == 1
        ) {
            SuccessResponse("Successfully updated.")
        } else {
            NotFoundResponse("Tournament with $tournamentId not found.")
        }

    @Transactional
    fun deleteTournament(tournamentId: Long): Response =
        if (tournamentRepository.deleteTournament(tournamentId) == 1) {
            SuccessResponse("Successfully deleted.")
        } else {
            NotFoundResponse("Tournament with $tournamentId not found.")
        }

    fun findAllGroupByTimeLine(latest: Int = 3): Map<String, List<TournamentResponse>> {
        val ongoing =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.ONGOING)
                .take(latest)
                .map { mapTournamentsWithParticipantsNumber(it) }
        val finished =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.FINISHED)
                .take(latest)
                .map { mapTournamentsWithParticipantsNumber(it) }
        val comingSoon =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.COMING_SOON)
                .take(latest)
                .map { mapTournamentsWithParticipantsNumber(it) }

        return mapOf(
            "ONGOING" to ongoing,
            "FINISHED" to finished,
            "COMING_SOON" to comingSoon
        )
    }

    fun getAllTournamentsByTimelinePaginated(
        pageable: Pageable,
        timelineTournamentType: TimelineTournamentType
    ): Page<TournamentResponse> {
        val tournaments =
            tournamentRepository.findAllByTimelineTypeOrderByDateCreatedDesc(timelineTournamentType, pageable)

        val list = if (timelineTournamentType == TimelineTournamentType.COMING_SOON) {
            tournaments.content
                .map { mapTournamentsWithParticipantsNumber(it) }
        } else {
            tournaments.content
                .map { mapTournamentsWithParticipantsNumber(it) }
        }

        return PageImpl(list, pageable, tournaments.totalElements)
    }

    @Transactional
    fun addTeamToTournament(teamId: Long, tournamentId: Long): Response =
        tournamentRepository.findByIdOrNull(tournamentId)?.let { tournament ->
            teamService.findTeamByIdOrNull(teamId)?.let { team ->
                val participants = teamTournamentRepository.findAllByTournamentId(tournament.id)
                if (participants.any { it.team.id == team.id }) {
                    BadRequestResponse("Already in tournament.")
                } else if (participants.size < tournament.numberOfParticipants - 1) {
                    teamTournamentRepository.save(TeamTournament(tournament = tournament, team = team))
                    SuccessResponse("Successfully added.")
                } else if (participants.size == tournament.numberOfParticipants - 1) {
                    teamTournamentRepository.save(TeamTournament(tournament = tournament, team = team))
                    val actualTeams = teamTournamentRepository.findAllByTournamentId(tournament.id)
                    generateTeamMatches(tournament, actualTeams)
                    SuccessResponse("Successfully joined.")
                } else {
                    BadRequestResponse("Tournament is full.")
                }
            } ?: NotFoundResponse("Team with $teamId was not found.")
        } ?: NotFoundResponse("Tournament with $tournamentId was not found.")

    @Transactional
    fun addUserToTournament(appUserId: Long, tournamentId: Long): Response =
        tournamentRepository.findByIdOrNull(tournamentId)?.let { tournament ->
            appUserService.findAppUserByIdOrNull(appUserId)?.let { appUser ->
                val participants = appUserTournamentRepository.findAllByTournamentId(tournament.id)
                if (participants.any { it.appUser.id == appUser.id }) {
                    BadRequestResponse("Already in tournament.")
                } else if (participants.size < tournament.numberOfParticipants - 1) {
                    appUserTournamentRepository.save(AppUserTournament(tournament = tournament, appUser = appUser))
                    SuccessResponse("Successfully added.")
                } else if (participants.size == tournament.numberOfParticipants - 1) {
                    appUserTournamentRepository.save(AppUserTournament(tournament = tournament, appUser = appUser))
                    val actualUsers = appUserTournamentRepository.findAllByTournamentId(tournament.id)
                    generateIndividualMatches(tournament, actualUsers)
                    SuccessResponse("Successfully joined.")
                } else {
                    BadRequestResponse("Tournament is full.")
                }
            } ?: NotFoundResponse("User with $appUserId was not found.")
        } ?: NotFoundResponse("Tournament with $tournamentId was not found.")

    fun generateTeamMatches(tournament: Tournament, participants: MutableList<TeamTournament>) {
        participants.shuffle()
        var tournamentSize = getNearestPowerOfTwo(participants) / 2
        val firstRoundSize = tournamentSize
        var matches = mutableListOf<TeamMatch>()
        var round = 0
        while (tournamentSize > 0) {
            for (i in 0 until tournamentSize) {
                matches.add(TeamMatch(number = i, round = round))
            }
            tournamentSize /= 2
            round++
        }

        matches = teamMatchRepository.saveAll(matches)

        tournamentSize = firstRoundSize
        var start = 0
        while (tournamentSize > 1) {
            for (i in start until start + tournamentSize) {
                val match = i / 2
                val nextMatchIndex = start / 2 + tournamentSize + match
                matches[i] = matches[i].copy(nextMatch = matches[nextMatchIndex])
            }
            start += tournamentSize
            tournamentSize /= 2
        }

        var teamIndex = 0
        for (i in 0 until firstRoundSize) {
            matches[i] = matches[i].copy(team1 = participants[teamIndex].team)
            teamIndex++
        }

        for (i in 0 until firstRoundSize) {
            if (teamIndex < participants.size) {
                matches[i] = matches[i].copy(team2 = participants[teamIndex].team)
                teamIndex++
            } else {
                matches[i] = matches[i].copy(isFinished = true)
                val match = i / 2
                val nextMatchIndex = firstRoundSize + match
                when (i % 2) {
                    0 -> {
                        matches[nextMatchIndex] = matches[nextMatchIndex].copy(team1 = matches[i].team1)
                    }
                    1 -> {
                        matches[nextMatchIndex] = matches[nextMatchIndex].copy(team2 = matches[i].team1)
                    }
                }
            }
        }

        val dbTeamMatches = teamMatchRepository.saveAll(matches)
        val teamMatchesTournament =
            dbTeamMatches.map { TeamMatchesTournament(tournament = tournament, teamMatch = it) }
        teamMatchesTournamentRepository.saveAll(teamMatchesTournament)
        teamTournamentRepository.deleteTeams(tournament.id)
        tournamentRepository.updateTournamentStatus(TimelineTournamentType.ONGOING, tournament.id)

    }

    @Transactional
    fun editMatch(editMatchRequest: EditMatchRequest): Response {

        val tournament = tournamentRepository.findByIdOrNull(editMatchRequest.tournamentId)

        tournament?.let {

            val user = SecurityContextHolder.getContext().authentication.principal as AppUser
            if (it.organizer != user) {
                return BadRequestResponse("No privileges to edit")
            }

            when (it.type) {
                TournamentType.TEAM -> {
                    val match = teamMatchRepository.findByIdOrNull(editMatchRequest.matchId)
                    match?.let {
                        val newMatch = match.copy(
                            winner = editMatchRequest.winner,
                            isFinished = editMatchRequest.isFinished,
                            score1 = editMatchRequest.score1,
                            score2 = editMatchRequest.score2,
                        )

                        newMatch.winner?.let {
                            val nextMatch = match.nextMatch
                            nextMatch?.let { match ->
                                when (newMatch.number % 2) {
                                    0 -> {
                                        teamMatchRepository.save(
                                            match.copy(team1 = if (newMatch.winner == 0) newMatch.team1 else newMatch.team2)
                                        )
                                    }
                                    1 -> {
                                        teamMatchRepository.save(
                                            match.copy(team2 = if (newMatch.winner == 0) newMatch.team1 else newMatch.team2)
                                        )
                                    }
                                    else -> {}
                                }
                            }
                        }

                        teamMatchRepository.save(newMatch)

                    } ?: return NotFoundResponse("Match not found")
                }
                TournamentType.INDIVIDUAL -> {
                    val match = individualMatchRepository.findByIdOrNull(editMatchRequest.matchId)
                    match?.let {
                        val newMatch = match.copy(
                            winner = editMatchRequest.winner,
                            isFinished = editMatchRequest.isFinished,
                            score1 = editMatchRequest.score1,
                            score2 = editMatchRequest.score2,
                        )

                        newMatch.winner?.let {
                            val nextMatch = match.nextMatch
                            nextMatch?.let {
                                when (newMatch.number % 2) {
                                    0 -> {
                                        individualMatchRepository.save(
                                            it.copy(player1 = if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)
                                        )
                                    }
                                    1 -> {
                                        individualMatchRepository.save(
                                            it.copy(player2 = if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)
                                        )
                                    }
                                    else -> {}
                                }
                            } ?: updateTournamentStatus(TimelineTournamentType.FINISHED, editMatchRequest.tournamentId)
                        }

                        individualMatchRepository.save(newMatch)
                    } ?: return NotFoundResponse("Match not found")
                }
            }
        } ?: return NotFoundResponse("Tournament not found")

        return SuccessResponse("Saved match")
    }

    fun getAllTournamentMatches(tournamentId: Long): Response {
        val tournament = tournamentRepository.findByIdOrNull(tournamentId)

        tournament?.let {
            when (tournament.type) {
                TournamentType.TEAM -> {
                    val matches = teamMatchesTournamentRepository.findAllMatchesByTournament(tournament)
                    return SuccessResponse(
                        BracketResponse(
                            if (tournament.timelineType == TimelineTournamentType.COMING_SOON) {
                                mapTournamentsWithParticipantsNumber(tournament)
                            } else {
                                mapTournamentsWithParticipantsNumber(tournament)
                            },
                            matches.sortedBy { it.number }
                                .map {
                                    MatchResponse(
                                        it.id,
                                        it.winner,
                                        it.isFinished,
                                        it.number,
                                        it.round,
                                        it.team1?.let { team -> OpponentResponse(team.id, team.name, it.score1) },
                                        it.team2?.let { team -> OpponentResponse(team.id, team.name, it.score2) }
                                    )
                                })
                    )
                }
                TournamentType.INDIVIDUAL -> {
                    val matches = individualMatchesTournamentRepository.findAllMatchesByTournament(tournament)
                    return SuccessResponse(
                        BracketResponse(
                            if (tournament.timelineType == TimelineTournamentType.COMING_SOON) {
                                mapTournamentsWithParticipantsNumber(tournament)
                            } else {
                                mapTournamentsWithParticipantsNumber(tournament)
                            },
                            matches.sortedBy { it.number }
                                .map {
                                    MatchResponse(
                                        it.id,
                                        it.winner,
                                        it.isFinished,
                                        it.number,
                                        it.round,
                                        it.player1?.let { player ->
                                            OpponentResponse(
                                                player.id,
                                                "${player.firstName} ${player.lastName}",
                                                it.score1
                                            )
                                        },
                                        it.player2?.let { player ->
                                            OpponentResponse(
                                                player.id,
                                                "${player.firstName} ${player.lastName}",
                                                it.score2
                                            )
                                        },
                                    )
                                })
                    )
                }
            }
        }

        return NotFoundResponse("Team not found")
    }

    fun generateIndividualMatches(tournament: Tournament, participants: MutableList<AppUserTournament>) {
        participants.shuffle()
        var tournamentSize = getNearestPowerOfTwo(participants) / 2
        val firstRoundSize = tournamentSize
        var matches = mutableListOf<IndividualMatch>()
        var round = 0
        while (tournamentSize > 0) {
            for (i in 0 until tournamentSize) {
                matches.add(IndividualMatch(number = i, round = round))
            }
            tournamentSize /= 2
            round++
        }

        matches = individualMatchRepository.saveAll(matches)

        tournamentSize = firstRoundSize
        var start = 0
        while (tournamentSize > 1) {
            for (i in start until start + tournamentSize) {
                val match = i / 2
                val nextMatchIndex = start / 2 + tournamentSize + match
                matches[i] = matches[i].copy(nextMatch = matches[nextMatchIndex])
            }
            start += tournamentSize
            tournamentSize /= 2
        }

        var playerIndex = 0
        for (i in 0 until firstRoundSize) {
            matches[i] = matches[i].copy(player1 = participants[playerIndex].appUser)
            playerIndex++
        }

        for (i in 0 until firstRoundSize) {
            if (playerIndex < participants.size) {
                matches[i] = matches[i].copy(player2 = participants[playerIndex].appUser)
                playerIndex++
            } else {
                matches[i] = matches[i].copy(isFinished = true)
                val match = i / 2
                val nextMatchIndex = firstRoundSize + match
                when (i % 2) {
                    0 -> {
                        matches[nextMatchIndex] = matches[nextMatchIndex].copy(player1 = matches[i].player1)
                    }
                    1 -> {
                        matches[nextMatchIndex] = matches[nextMatchIndex].copy(player2 = matches[i].player1)
                    }
                }
            }
        }

        val dbAppUserMatches = individualMatchRepository.saveAll(matches)
        val individualMatchesTournament =
            dbAppUserMatches.map {
                IndividualMatchesTournament(
                    tournament = tournament,
                    individualMatch = it
                )
            }
        individualMatchesTournamentRepository.saveAll(individualMatchesTournament)
        appUserTournamentRepository.deleteAppUsers(tournament.id)
        tournamentRepository.updateTournamentStatus(TimelineTournamentType.ONGOING, tournament.id)
    }

    private fun getNearestPowerOfTwo(participants: MutableList<*>): Int {
        val size = participants.size.toDouble()
        return 2.0.pow(ceil(log2(size))).toInt()
    }


    @Transactional
    fun updateTournamentStatus(tournamentTimelineType: TimelineTournamentType, tournamentId: Long) =
        tournamentRepository.updateTournamentStatus(tournamentTimelineType, tournamentId)


    private fun mapTournamentsWithParticipantsNumber(tournament: Tournament): TournamentResponse =
        when (tournament.type) {
            TournamentType.INDIVIDUAL -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                if (tournament.timelineType == TimelineTournamentType.COMING_SOON) {
                    appUserTournamentRepository.findAllByTournamentId(tournament.id).map {
                        "${it.appUser.firstName} ${it.appUser.lastName}"
                    }
                } else {
                    playersInIndividualTournamentRepository.findAllByTournamentId(tournament.id).map {
                        "${it.firstName} ${it.lastName}"
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
                        it.team.name
                    }
                } else {
                    teamsInTournamentRepository.findAllByTournamentId(tournament.id).map {
                        it.teamName
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
}
