package com.sorsix.koko.api

import com.sorsix.koko.dto.request.CreateTeamRequest
import com.sorsix.koko.dto.response.BadRequestResponse
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.service.TeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/team")
class TeamController(val teamService: TeamService) {

    @PostMapping("/create")
    fun createTeam(@RequestBody createTeamRequest: CreateTeamRequest): ResponseEntity<Response> {
        return when(val result = teamService.createTeam(createTeamRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
    }
}