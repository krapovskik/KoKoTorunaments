package com.sorsix.koko.service

import com.sorsix.koko.domain.AppUserTeams
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
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
                .map { AppUserTeams::appUser }
            SuccessResponse(players)
        } ?: NotFoundResponse("Team with $teamId not found.")

    fun findAllTeamsByPlayer(playerId: Long): Response =
        appUserService.findAppUserByIdOrNull(playerId)?.let {
            val teams = appUserTeamsRepository.findAppUserTeamsByAppUser(it)
                .map { AppUserTeams::team }
            SuccessResponse(teams)
        } ?: NotFoundResponse("Team with $playerId not found.")

    fun addPlayerToTeam(teamId: Long, playerId: Long): Response =
        teamService.findTeamByIdOrNull(teamId)?.let { team ->
            appUserService.findAppUserByIdOrNull(playerId)?.let { appUser ->
                val saved = appUserTeamsRepository.save(AppUserTeams(team = team, appUser = appUser))
                SuccessResponse(saved)
            } ?: NotFoundResponse("User with $playerId not found.")
        } ?: NotFoundResponse("Team with $teamId not found.")
}
