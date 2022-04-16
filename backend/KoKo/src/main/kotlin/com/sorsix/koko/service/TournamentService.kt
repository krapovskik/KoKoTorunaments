package com.sorsix.koko.service

import com.sorsix.koko.domain.*
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.*
import com.sorsix.koko.repository.view.PlayersInIndividualTournamentRepository
import com.sorsix.koko.repository.view.TeamsInTournamentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
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

    fun createTournament(
        tournamentName: String,
        tournamentCategory: String,
        numberOfParticipants: Int,
        tournamentType: TournamentType,
        tournamentTimelineType: TimelineTournamentType
    ): Response = SuccessResponse(
        tournamentRepository.save(
            Tournament(
                name = tournamentName,
                category = tournamentCategory,
                numberOfParticipants = numberOfParticipants,
                type = tournamentType,
                timelineType = tournamentTimelineType
            )
        )
    )

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
                .map { mapTournamentsWithParticipantsNumberComingSoon(it) }

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

        val list = tournaments.content
            .map { mapTournamentsWithParticipantsNumber(it) }

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
                    val teamMatches = generateTeamMatches(actualTeams)
                    val dbTeamMatches = teamMatchRepository.saveAll(teamMatches)
                    val teamMatchesTournament =
                        dbTeamMatches.map { TeamMatchesTournament(tournament = tournament, teamMatch = it) }
                    teamMatchesTournamentRepository.saveAll(teamMatchesTournament)
                    teamTournamentRepository.deleteTeams(tournament.id)
                    tournamentRepository.updateTournamentStatus(TimelineTournamentType.ONGOING, tournament.id)
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
                    val individualMatches = generateIndividualMatches(actualUsers)
                    val dbAppUserMatches = individualMatchRepository.saveAll(individualMatches)
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
                    SuccessResponse("Successfully joined.")
                } else {
                    BadRequestResponse("Tournament is full.")
                }
            } ?: NotFoundResponse("User with $appUserId was not found.")
        } ?: NotFoundResponse("Tournament with $tournamentId was not found.")

    fun generateTeamMatches(participants: MutableList<TeamTournament>): MutableList<TeamMatch> {
        participants.shuffle()
        var tournamentSize = getNearestPowerOfTwo(participants) / 2
        val firstRoundSize = tournamentSize
        val matches = mutableListOf<TeamMatch>()
        var round = 0
        while (tournamentSize > 0) {
            for (i in 0 until tournamentSize) {
                matches.add(TeamMatch(number = i, round = round))
            }
            tournamentSize /= 2
            round++
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

        return matches
    }

    fun getAllTournamentMatches(tournamentId: Long): Response {
        val tournament = tournamentRepository.findByIdOrNull(tournamentId)

        tournament?.let {
            when (tournament.type) {
                TournamentType.TEAM -> {
                    val matches = teamMatchesTournamentRepository.findAllMatchesByTournament(tournament)
                    return SuccessResponse(
                        BracketResponse(
                            mapTournamentsWithParticipantsNumber(tournament),
                            matches.sortedBy { it.number }
                                .map {
                                    MatchResponse(
                                        it.id,
                                        it.winner,
                                        it.isFinished,
                                        it.number,
                                        it.round,
                                        it.team1?.let { team -> OpponentResponse(team.id, team.name) },
                                        it.team2?.let { team -> OpponentResponse(team.id, team.name) }
                                    )
                                })
                    )
                }
                TournamentType.INDIVIDUAL -> {
                    val matches = individualMatchesTournamentRepository.findAllMatchesByTournament(tournament)
                    return SuccessResponse(
                        BracketResponse(
                            mapTournamentsWithParticipantsNumber(tournament),
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
                                                "${player.firstName} ${player.lastName}"
                                            )
                                        },
                                        it.player2?.let { player ->
                                            OpponentResponse(
                                                player.id,
                                                "${player.firstName} ${player.lastName}"
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

    fun generateIndividualMatches(participants: MutableList<AppUserTournament>): MutableList<IndividualMatch> {
        participants.shuffle()
        var tournamentSize = getNearestPowerOfTwo(participants) / 2
        val firstRoundSize = tournamentSize
        val matches = mutableListOf<IndividualMatch>()
        var round = 0
        while (tournamentSize > 0) {
            for (i in 0 until tournamentSize) {
                matches.add(IndividualMatch(number = i, round = round))
            }
            tournamentSize /= 2
            round++
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

        return matches
    }

    private fun getNearestPowerOfTwo(participants: MutableList<*>): Int {
        val size = participants.size.toDouble()
        return 2.0.pow(ceil(log2(size))).toInt()
    }


    @Transactional
    fun updateTournamentStatus(tournamentTimelineType: TimelineTournamentType, tournamentId: Long) =
        tournamentRepository.updateTournamentStatus(tournamentTimelineType, tournamentId)


    private fun mapTournamentsWithParticipantsNumberComingSoon(tournament: Tournament): TournamentResponse =
        when (tournament.type) {
            TournamentType.INDIVIDUAL -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                appUserTournamentRepository.findAllByTournamentId(tournament.id).size,
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name
            )
            TournamentType.TEAM -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                teamTournamentRepository.findAllByTournamentId(tournament.id).size,
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name
            )
        }

    private fun mapTournamentsWithParticipantsNumber(tournament: Tournament): TournamentResponse =
        when (tournament.type) {
            TournamentType.INDIVIDUAL -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                playersInIndividualTournamentRepository.findAllByTournamentId(tournament.id).size,
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name
            )
            TournamentType.TEAM -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                teamsInTournamentRepository.findAllByTournamentId(tournament.id).size,
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name
            )
        }
}
