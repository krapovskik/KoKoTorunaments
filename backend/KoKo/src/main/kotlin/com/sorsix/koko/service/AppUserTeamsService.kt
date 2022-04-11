package com.sorsix.koko.service

import com.sorsix.koko.domain.AppUserTeams
import com.sorsix.koko.dto.request.AddUserToTeamRequest
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.AppUserTeamsRepository
import org.springframework.stereotype.Service

@Service
class AppUserTeamsService
    (
    val teamService: TeamService,
    val appUserService: AppUserService,
    val appUserTeamsRepository: AppUserTeamsRepository
) {

    fun findAllPlayersByTeam(teamId: Long): Response =
        teamService.findTeamByIdOrNull(teamId)?.let {
            val players = appUserTeamsRepository.findAppUserTeamsByTeam(it)
                .filter { user -> user.appUser.isActive }
                .map { userTeams -> TeamMemberResponse("${userTeams.appUser.firstName} ${userTeams.appUser.lastName}-${userTeams.appUser.id}") }
            SuccessResponse(players)
        } ?: NotFoundResponse("Team with $teamId not found.")

    fun findAllTeamsByPlayer(playerId: Long): Response =
        appUserService.findAppUserByIdOrNull(playerId)?.let {
            val teams = appUserTeamsRepository.findAppUserTeamsByAppUser(it)
                .map { AppUserTeams::team }
            SuccessResponse(teams)
        } ?: NotFoundResponse("Player with $playerId not found.")

    fun addPlayerToTeam(addUserToTeamRequest: AddUserToTeamRequest): Response {
        val (userId, teamId) = addUserToTeamRequest
        return teamService.findTeamByIdOrNull(teamId)?.let { team ->
            appUserService.findAppUserByIdOrNull(userId)?.let { appUser ->
                if (appUserTeamsRepository.existsByAppUserAndTeam(appUser, team)) {
                    return BadRequestResponse("Player already in team")
                }
                appUserTeamsRepository.save(AppUserTeams(team = team, appUser = appUser))
                SuccessResponse("Player added to team")
            } ?: NotFoundResponse("User not found.")
        } ?: NotFoundResponse("Team not found.")
    }
}
