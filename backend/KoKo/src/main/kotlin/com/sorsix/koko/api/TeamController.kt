package com.sorsix.koko.api

import com.sorsix.koko.dto.request.AddUserToTeamRequest
import com.sorsix.koko.dto.request.CreateTeamRequest
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.service.AppUserTeamsService
import com.sorsix.koko.service.TeamService
import com.sorsix.koko.util.MapperService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/team")
class TeamController(
    val teamService: TeamService,
    val appUserTeamsService: AppUserTeamsService,
    val mapperService: MapperService,
) {

    @PostMapping("/create")
    fun createTeam(@RequestBody createTeamRequest: CreateTeamRequest): ResponseEntity<out Response> {
        val result = teamService.createTeam(createTeamRequest)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/myTeams")
    fun getMyTeams() = teamService.getMyTeams()

    @PostMapping("/addUserToTeam")
    fun addUserToTeam(@RequestBody addUserToTeamRequest: AddUserToTeamRequest): ResponseEntity<out Response> {
        val result = appUserTeamsService.addPlayerToTeam(addUserToTeamRequest)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/userTeams")
    fun getTeamsForUser() = teamService.getTeamsForUser()

    @GetMapping("{teamId}/players")
    fun getPlayersInTeam(@PathVariable teamId: Long): ResponseEntity<out Response> {
        val result = appUserTeamsService.findAllPlayersByTeam(teamId)
        return mapperService.mapResponseToResponseEntity(result)
    }
}
