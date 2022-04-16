package com.sorsix.koko.api

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.dto.request.JoinTeamTournamentRequest
import com.sorsix.koko.dto.request.JoinUserTournamentRequest
import com.sorsix.koko.dto.response.*
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
    fun addTeamToTournament(@RequestBody request: JoinTeamTournamentRequest): ResponseEntity<Response> =
        when (val result = this.tournamentService.addTeamToTournament(request.teamId, request.tournamentId)) {
            is SuccessResponse<*> -> ResponseEntity.ok(result)
            is NotFoundResponse -> ResponseEntity(result, HttpStatus.NOT_FOUND)
            is BadRequestResponse -> ResponseEntity.badRequest().body(result)
        }

    @PostMapping("/addPlayer")
    fun addPlayerToTournament(@RequestBody request: JoinUserTournamentRequest): ResponseEntity<Response> =
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
}
