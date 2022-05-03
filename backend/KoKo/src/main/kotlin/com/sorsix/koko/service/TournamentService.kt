package com.sorsix.koko.service

import com.sorsix.koko.domain.*
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.request.CreateTournamentRequest
import com.sorsix.koko.dto.request.EditMatchRequest
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.*
import com.sorsix.koko.repository.view.PlayersInIndividualTournamentRepository
import com.sorsix.koko.repository.view.PlayersInTeamTournamentRepository
import com.sorsix.koko.util.MapperService
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
    val teamTournamentRepository: TeamTournamentRepository,
    val individualTournamentRepository: IndividualTournamentRepository,
    val teamService: TeamService,
    val appUserService: AppUserService,
    val teamMatchRepository: TeamMatchRepository,
    val teamMatchesTournamentRepository: TeamMatchesTournamentRepository,
    val individualMatchRepository: IndividualMatchRepository,
    val individualMatchesTournamentRepository: IndividualMatchesTournamentRepository,
    val individualWinnerRepository: IndividualWinnerRepository,
    val teamWinnerRepository: TeamWinnerRepository,
    val appUserTeamsRepository: AppUserTeamsRepository,
    val playersInTeamTournamentRepository: PlayersInTeamTournamentRepository,
    val mapperService: MapperService,
) {

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

    fun findAllGroupByTimeLine(latest: Int = 3): Map<String, List<TournamentResponse>> {
        val ongoing =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.ONGOING)
                .take(latest)
                .map { mapperService.mapTournamentsWithParticipantsNumber(it) }
        val finished =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.FINISHED)
                .take(latest)
                .map { mapperService.mapTournamentsWithParticipantsNumber(it) }
        val comingSoon =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.COMING_SOON)
                .take(latest)
                .map { mapperService.mapTournamentsWithParticipantsNumber(it) }

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
                .map { mapperService.mapTournamentsWithParticipantsNumber(it) }
        } else {
            tournaments.content
                .map { mapperService.mapTournamentsWithParticipantsNumber(it) }
        }

        return PageImpl(list, pageable, tournaments.totalElements)
    }

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

        matches = generateMatches(firstRoundSize, matches, participants)

        val dbTeamMatches = teamMatchRepository.saveAll(matches)
        val teamMatchesTournament =
            dbTeamMatches.map { TeamMatchesTournament(tournament = tournament, teamMatch = it) }
        teamMatchesTournamentRepository.saveAll(teamMatchesTournament)
        teamTournamentRepository.deleteTeams(tournament.id)
        tournamentRepository.updateTournamentStatus(TimelineTournamentType.ONGOING, tournament.id)
    }

    fun generateIndividualMatches(tournament: Tournament, participants: MutableList<IndividualTournament>) {
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

        matches = generateMatches(firstRoundSize, matches, participants)

        val dbAppUserMatches = individualMatchRepository.saveAll(matches)
        val individualMatchesTournament =
            dbAppUserMatches.map {
                IndividualMatchesTournament(
                    tournament = tournament,
                    individualMatch = it
                )
            }
        individualMatchesTournamentRepository.saveAll(individualMatchesTournament)
        individualTournamentRepository.deleteAppUsers(tournament.id)
        tournamentRepository.updateTournamentStatus(TimelineTournamentType.ONGOING, tournament.id)
    }

    private fun <T : Match<T>, U : TypedTournament> generateMatches(
        firstRoundSize: Int,
        matches: MutableList<T>,
        participants: MutableList<U>
    ): MutableList<T> {
        var tournamentSize = firstRoundSize
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
            matches[i] = matches[i].copy(player1 = participants[teamIndex].player)
            teamIndex++
        }

        for (i in 0 until firstRoundSize) {
            if (teamIndex < participants.size) {
                matches[i] = matches[i].copy(player2 = participants[teamIndex].player)
                teamIndex++
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
    fun addTeamToTournament(teamId: Long, tournamentId: Long): Response =
        tournamentRepository.findByIdOrNull(tournamentId)?.let { tournament ->
            teamService.findTeamByIdOrNull(teamId)?.let { team ->
                val participants = teamTournamentRepository.findAllByTournamentId(tournament.id)
                if (participants.any { it.player.id == team.id }) {
                    BadRequestResponse("Already in tournament.")
                } else if (participants.size < tournament.numberOfParticipants - 1) {
                    teamTournamentRepository.save(TeamTournament(tournament = tournament, player = team))
                    SuccessResponse("Successfully added.")
                } else if (participants.size == tournament.numberOfParticipants - 1) {
                    teamTournamentRepository.save(TeamTournament(tournament = tournament, player = team))
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
                val participants = individualTournamentRepository.findAllByTournamentId(tournament.id)
                if (participants.any { it.player.id == appUser.id }) {
                    BadRequestResponse("Already in tournament.")
                } else if (participants.size < tournament.numberOfParticipants - 1) {
                    individualTournamentRepository.save(
                        IndividualTournament(
                            tournament = tournament,
                            player = appUser
                        )
                    )
                    SuccessResponse("Successfully added.")
                } else if (participants.size == tournament.numberOfParticipants - 1) {
                    individualTournamentRepository.save(
                        IndividualTournament(
                            tournament = tournament,
                            player = appUser
                        )
                    )
                    val actualUsers = individualTournamentRepository.findAllByTournamentId(tournament.id)
                    generateIndividualMatches(tournament, actualUsers)
                    SuccessResponse("Successfully joined.")
                } else {
                    BadRequestResponse("Tournament is full.")
                }
            } ?: NotFoundResponse("User with $appUserId was not found.")
        } ?: NotFoundResponse("Tournament with $tournamentId was not found.")

    @Transactional
    fun editMatch(editMatchRequest: EditMatchRequest): Response {

        val matchTournament = tournamentRepository.findByIdOrNull(editMatchRequest.tournamentId)
        matchTournament?.let { tournament ->
            val user = SecurityContextHolder.getContext().authentication.principal as AppUser
            if (tournament.organizer != user) {
                return BadRequestResponse("No privileges to edit")
            }

            when (tournament.type) {
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
                            nextMatch?.let {
                                if (newMatch.number % 2 == 0)
                                    teamMatchRepository.save(
                                        it.copy(player1 = if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)
                                    )
                                else
                                    teamMatchRepository.save(
                                        it.copy(player2 = if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)
                                    )
                            } ?: run {
                                updateTournamentStatus(TimelineTournamentType.FINISHED, tournament.id)
                                teamWinnerRepository.save(
                                    TeamWinner(
                                        tournament = tournament,
                                        team = (if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)!!
                                    )
                                )
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
                                if (newMatch.number % 2 == 0)
                                    individualMatchRepository.save(
                                        it.copy(player1 = if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)
                                    )
                                else
                                    individualMatchRepository.save(
                                        it.copy(player2 = if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)
                                    )
                            } ?: run {
                                updateTournamentStatus(TimelineTournamentType.FINISHED, editMatchRequest.tournamentId)
                                individualWinnerRepository.save(
                                    IndividualWinner(
                                        tournament = tournament,
                                        appUser = (if (newMatch.winner == 0) newMatch.player1 else newMatch.player2)!!
                                    )
                                )
                            }
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
            val matches = when (tournament.type) {
                TournamentType.TEAM -> {
                    teamMatchesTournamentRepository.findAllMatchesByTournament(tournament)
                }
                TournamentType.INDIVIDUAL -> {
                    individualMatchesTournamentRepository.findAllMatchesByTournament(tournament)
                }
            }

            return SuccessResponse(
                BracketResponse(
                    mapperService.mapTournamentsWithParticipantsNumber(tournament),
                    matches.sortedBy { it.number }
                        .map {
                            mapperService.mapToMatchResponse(it)
                        })
            )
        }

        return NotFoundResponse("Team not found")
    }

    @Transactional
    fun updateTournamentStatus(tournamentTimelineType: TimelineTournamentType, tournamentId: Long) =
        tournamentRepository.updateTournamentStatus(tournamentTimelineType, tournamentId)

    fun getWonTournamentsByUser(id: Long, pageable: Pageable): Page<ProfileTournamentResponse> {
        val user = appUserService.findAppUserByIdOrNull(id)
        user?.let {
            val tournaments = getWonTournamentsByUser(it)
            return createProfilePagination(tournaments, pageable)
        }

        return PageImpl(listOf())
    }

    fun getAllTournamentsByUser(id: Long, pageable: Pageable): Page<ProfileTournamentResponse> {
        val user = appUserService.findAppUserByIdOrNull(id)
        user?.let {
            val tournaments = getAllTournamentsByUser(id)
            return createProfilePagination(tournaments, pageable)
        }
        return PageImpl(listOf())
    }

    fun getProfileStatistics(id: Long) = appUserService.findAppUserByIdOrNull(id)?.let {
        val all = getAllTournamentsByUser(id)
        val won = getWonTournamentsByUser(it).size
        val loss =
            all.filter { tournament -> tournament.timelineTournamentType == TimelineTournamentType.FINISHED }.size - won
        val others = all.size - won - loss

        SuccessResponse(
            ProfileStatisticsResponse(
                "${it.firstName} ${it.lastName}",
                won,
                loss,
                others,
                it.profilePhoto
            )
        )
    } ?: NotFoundResponse("User not found")

    private fun getAllTournamentsByUser(id: Long): List<ProfileTournamentResponse> {
        val tournamentIds = playersInIndividualTournamentRepository.findAllByPlayerId(id) +
                playersInTeamTournamentRepository.findAllByPlayerId(id)

        return tournamentRepository.findAllByIdIn(tournamentIds)
            .sortedByDescending {
                it.dateCreated
            }.map {
                mapperService.mapToProfileTournamentResponse(it)
            }
    }

    private fun getWonTournamentsByUser(user: AppUser): List<ProfileTournamentResponse> {

        val individualWins = individualWinnerRepository.findAllByAppUser(user).map {
            it.tournament
        }
        val teams = appUserTeamsRepository.findTeamsForUser(user).map {
            it.team
        }
        val teamsWins = teamWinnerRepository.findAllByTeamIn(teams).map {
            it.tournament
        }

        return (individualWins + teamsWins)
            .sortedBy {
                it.dateCreated
            }
            .map {
                mapperService.mapToProfileTournamentResponse(it)
            }
    }

    private fun createProfilePagination(
        list: List<ProfileTournamentResponse>,
        pageable: Pageable
    ): Page<ProfileTournamentResponse> {
        val start = pageable.pageNumber * pageable.pageSize
        val end = if (start + pageable.pageSize > list.size) list.size else start + pageable.pageSize
        if (start < end) {
            return PageImpl(list.subList(start, end), pageable, list.size.toLong())
        }

        return PageImpl(listOf(), pageable, list.size.toLong())
    }
}
