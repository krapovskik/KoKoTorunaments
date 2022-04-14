package com.sorsix.koko.service

import com.sorsix.koko.domain.*
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.domain.view.PlayersInIndividualTournament
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

        val list = tournaments.content
            .map { mapTournamentsWithParticipantsNumber(it) }

        return PageImpl(list, pageable, tournaments.totalElements)
    }

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
                    val teamMatches = mutableListOf<TeamMatch>()
                    val actualTeams = teamTournamentRepository.findAll()
                    actualTeams.shuffle()
                    for (i in 0 until actualTeams.size - 1 step 2) {
                        teamMatches.add(TeamMatch(team1 = actualTeams[i].team, team2 = actualTeams[i + 1].team))
                    }
                    val dbTeamMatches = teamMatchRepository.saveAll(teamMatches)
                    val teamMatchesTournament =
                        dbTeamMatches.map { TeamMatchesTournament(tournament = tournament, teamMatch = it) }
                    teamMatchesTournamentRepository.saveAll(teamMatchesTournament)
                    teamTournamentRepository.deleteTeams(tournament.id)
                    tournamentRepository.updateTournamentStatus(TimelineTournamentType.ONGOING, tournament.id)
                    SuccessResponse("Successfully.")
                } else {
                    BadRequestResponse("Tournament is full.")
                }
            } ?: NotFoundResponse("Team with $teamId was not found.")
        } ?: NotFoundResponse("Tournament with $tournamentId was not found.")

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
                    val individualMatches = mutableListOf<IndividualMatch>()
                    val actualUsers = appUserTournamentRepository.findAll()
                    actualUsers.shuffle()
                    for (i in 0 until actualUsers.size - 1 step 2) {
                        individualMatches.add(
                            IndividualMatch(
                                player1 = actualUsers[i].appUser,
                                player2 = actualUsers[i + 1].appUser
                            )
                        )
                    }
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
                    SuccessResponse("Successfully")
                } else {
                    BadRequestResponse("Tournament is full.")
                }
            } ?: NotFoundResponse("User with $appUserId was not found.")
        } ?: NotFoundResponse("Tournament with $tournamentId was not found.")


    @Transactional
    fun updateTournamentStatus(tournamentTimelineType: TimelineTournamentType, tournamentId: Long) =
        tournamentRepository.updateTournamentStatus(tournamentTimelineType, tournamentId)

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