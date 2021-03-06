package com.sorsix.koko.service

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.AppUserTeams
import com.sorsix.koko.domain.Team
import com.sorsix.koko.dto.request.CreateTeamRequest
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.AppUserTeamsRepository
import com.sorsix.koko.repository.TeamRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class TeamService(
    val teamRepository: TeamRepository,
    val appUserTeamsRepository: AppUserTeamsRepository
) {

    fun findTeamByIdOrNull(teamId: Long): Team? = teamRepository.findByIdOrNull(teamId)

    fun createTeam(createTeamRequest: CreateTeamRequest): Response {

        val teamName = createTeamRequest.teamName
        if (teamRepository.existsByName(teamName)) {
            return BadRequestResponse("Team already exists")
        }

        val team = teamRepository.save(Team(name = teamName))
        val user = SecurityContextHolder.getContext().authentication.principal as AppUser
        appUserTeamsRepository.save(AppUserTeams(team = team, appUser = user))
        return SuccessResponse("Team created successfully")
    }

    fun getTeamsForUser(): List<TeamResponse> {
        val user = SecurityContextHolder.getContext().authentication.principal as AppUser
        val appUserTeams = appUserTeamsRepository.findAppUserTeamsByAppUser(user)

        return appUserTeams.map {
            TeamResponse(
                it.team.id,
                it.team.name
            )
        }
    }

    fun getMyTeams(): List<MyTeamsResponse> {
        val user = SecurityContextHolder.getContext().authentication.principal as AppUser
        val appUserTeams = appUserTeamsRepository.findTeamsForUser(user)

        return appUserTeams
            .groupBy { it.team }
            .entries
            .map {
                MyTeamsResponse(
                    it.key.id,
                    it.key.name,
                    it.value.filter { user ->
                        user.appUser.isActive
                    }.map { value ->
                        TeamMemberResponse("${value.appUser.firstName} ${value.appUser.lastName}-${value.appUser.id}")
                    }
                )
            }
    }
}
