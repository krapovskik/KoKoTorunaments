package com.sorsix.koko.service

import com.sorsix.koko.domain.Team
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.repository.TeamRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TeamService(
    val teamRepository: TeamRepository
) {

    fun findAll(): List<Team> = teamRepository.findAll()

    fun findTeamByIdOrNull(teamId: Long): Team? = teamRepository.findByIdOrNull(teamId)

    fun createTeam(teamName: String): Response = SuccessResponse(teamRepository.save(Team(name = teamName)))

    @Transactional
    fun deleteTeam(teamId: Long): Response =
        if (teamRepository.updateTeamStatus(teamId, false) == 1) {
            SuccessResponse("Successfully status changed.")
        } else {
            NotFoundResponse("Team with $teamId not found.")
        }


    @Transactional
    fun updateTeam(teamId: Long, teamName: String): Response =
        if (teamRepository.updateTeamName(teamId, teamName) == 1) {
            SuccessResponse("Successfully updated.")
        } else {
            NotFoundResponse("Team with $teamId not found.")
        }


    @Transactional
    fun updateTeamStatus(teamId: Long, teamStatus: Boolean): Response =
        if (teamRepository.updateTeamStatus(teamId, teamStatus) == 1) {
            SuccessResponse("Successfully updated.")
        } else {
            NotFoundResponse("Team with $teamId not found.")
        }


}