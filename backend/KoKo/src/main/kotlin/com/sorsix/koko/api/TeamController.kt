package com.sorsix.koko.api

import com.sorsix.koko.dto.request.AddUserToTeamRequest
import com.sorsix.koko.dto.request.CreateTeamRequest
import com.sorsix.koko.dto.response.BadRequestResponse
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.service.AppUserTeamsService
import com.sorsix.koko.service.TeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/team")
class TeamController(val teamService: TeamService, val appUserTeamsService: AppUserTeamsService) {

    @PostMapping("/create")
    fun createTeam(@RequestBody createTeamRequest: CreateTeamRequest): ResponseEntity<Response> {
        return when (val result = teamService.createTeam(createTeamRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @GetMapping("/myTeams")
    fun getMyTeams() = teamService.getMyTeams()

    @PostMapping("/addUserToTeam")
    fun addUserToTeam(@RequestBody addUserToTeamRequest: AddUserToTeamRequest): ResponseEntity<Response> {
        return when (val result = appUserTeamsService.addPlayerToTeam(addUserToTeamRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }

    @GetMapping("/userTeams")
    fun getTeamsForUser() = teamService.getTeamsForUser()


    @GetMapping("{teamId}/players")
    fun getPlayersInTeam(@PathVariable teamId: Long): ResponseEntity<Response> {
        return when (val result = appUserTeamsService.findAllPlayersByTeam(teamId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }
}
