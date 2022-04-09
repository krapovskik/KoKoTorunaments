package com.sorsix.koko.service

import com.sorsix.koko.domain.Team
import com.sorsix.koko.dto.request.CreateTeamRequest
import com.sorsix.koko.dto.response.BadRequestResponse
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

    fun createTeam(createTeamRequest: CreateTeamRequest): Response {

        val teamName = createTeamRequest.teamName
        if(teamRepository.existsByName(teamName)) {
            return BadRequestResponse("Team already exists")
        }

        teamRepository.save(Team(name = teamName))
        return SuccessResponse("Team created successfully")
    }

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