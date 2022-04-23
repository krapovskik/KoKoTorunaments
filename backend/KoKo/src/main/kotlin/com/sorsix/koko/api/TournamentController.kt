package com.sorsix.koko.api

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.request.CreateTournamentRequest
import com.sorsix.koko.dto.request.EditMatchRequest
import com.sorsix.koko.dto.request.JoinTeamTournamentRequest
import com.sorsix.koko.dto.request.JoinUserTournamentRequest
import com.sorsix.koko.dto.response.BadRequestResponse
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.dto.response.TournamentResponse
import com.sorsix.koko.service.TournamentService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tournament")
class TournamentController(val tournamentService: TournamentService) {

    @GetMapping
    fun findAllGrouped(): Map<String, List<TournamentResponse>> = tournamentService.findAllGroupByTimeLine()

    @GetMapping("/ongoing")
    fun getOngoingTournaments(pageable: Pageable) =
        tournamentService.getAllTournamentsByTimelinePaginated(pageable, TimelineTournamentType.ONGOING)

    @GetMapping("/finished")
    fun getFinishedTournaments(pageable: Pageable) =
        tournamentService.getAllTournamentsByTimelinePaginated(pageable, TimelineTournamentType.FINISHED)

    @GetMapping("/comingSoon")
    fun getComingSoonTournaments(pageable: Pageable) =
        tournamentService.getAllTournamentsByTimelinePaginated(pageable, TimelineTournamentType.COMING_SOON)

    @PostMapping("/addTeam")
    fun addTeamToTournament(@RequestBody request: JoinTeamTournamentRequest) =
        when (val result = this.tournamentService.addTeamToTournament(request.teamId, request.tournamentId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }

    @PostMapping("/addPlayer")
    fun addPlayerToTournament(@RequestBody request: JoinUserTournamentRequest) =
        when (val result = this.tournamentService.addUserToTournament(request.appUserId, request.tournamentId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }

    @GetMapping("/bracket/{id}")
    fun getTournamentBracket(@PathVariable id: Long) =
        when (val result = this.tournamentService.getAllTournamentMatches(id)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }

    @PostMapping("/editMatch")
    fun editMatch(@RequestBody editMatchRequest: EditMatchRequest) =
        when (val result = this.tournamentService.editMatch(editMatchRequest)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }

    @GetMapping("/tournamentTypes")
    fun getTournamentType(): List<String> = TournamentType.values().map { it.name }

    @PostMapping("/createTournament")
    fun createTournament(@RequestBody request: CreateTournamentRequest) =
        when (val result = this.tournamentService.createTournament(request)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }

    @GetMapping("/profile/{id}/won")
    fun getWonTournamentsByUser(@PathVariable id: Long, pageable: Pageable) =
        tournamentService.getWonTournamentsByUser(id, pageable)

    @GetMapping("/profile/{id}/all")
    fun getAllTournamentsByUser(@PathVariable id: Long, pageable: Pageable) =
        tournamentService.getAllTournamentsByUser(id, pageable)

    @GetMapping("/profile/{id}/statistics")
    fun getProfileStatistics(@PathVariable id: Long) =
        when (val result = this.tournamentService.getProfileStatistics(id)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }
}
