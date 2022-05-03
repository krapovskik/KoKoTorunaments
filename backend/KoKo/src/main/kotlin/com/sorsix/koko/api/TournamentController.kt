package com.sorsix.koko.api

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.request.CreateTournamentRequest
import com.sorsix.koko.dto.request.EditMatchRequest
import com.sorsix.koko.dto.request.JoinTeamTournamentRequest
import com.sorsix.koko.dto.request.JoinUserTournamentRequest
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.service.TournamentService
import com.sorsix.koko.util.MapperService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/tournament")
class TournamentController(val tournamentService: TournamentService, val mapperService: MapperService) {

    @GetMapping
    fun findAllGrouped() = tournamentService.findAllGroupByTimeLine()

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
    fun addTeamToTournament(@RequestBody request: JoinTeamTournamentRequest): ResponseEntity<out Response> {
        val result = this.tournamentService.addTeamToTournament(request.teamId, request.tournamentId)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @PostMapping("/addPlayer")
    fun addPlayerToTournament(@RequestBody request: JoinUserTournamentRequest): ResponseEntity<out Response> {
        val result = this.tournamentService.addUserToTournament(request.appUserId, request.tournamentId)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/bracket/{id}")
    fun getTournamentBracket(@PathVariable id: Long): ResponseEntity<out Response> {
        val result = this.tournamentService.getAllTournamentMatches(id)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @PostMapping("/editMatch")
    fun editMatch(@RequestBody editMatchRequest: EditMatchRequest): ResponseEntity<out Response> {
        val result = this.tournamentService.editMatch(editMatchRequest)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/tournamentTypes")
    fun getTournamentType(): List<String> = TournamentType.values().map { it.name }

    @PostMapping("/createTournament")
    fun createTournament(@RequestBody request: CreateTournamentRequest): ResponseEntity<out Response> {
        val result = this.tournamentService.createTournament(request)
        return mapperService.mapResponseToResponseEntity(result)
    }

    @GetMapping("/profile/{id}/won")
    fun getWonTournamentsByUser(@PathVariable id: Long, pageable: Pageable) =
        tournamentService.getWonTournamentsByUser(id, pageable)

    @GetMapping("/profile/{id}/all")
    fun getAllTournamentsByUser(@PathVariable id: Long, pageable: Pageable) =
        tournamentService.getAllTournamentsByUser(id, pageable)

    @GetMapping("/profile/{id}/statistics")
    fun getProfileStatistics(@PathVariable id: Long): ResponseEntity<out Response> {
        val result = this.tournamentService.getProfileStatistics(id)
        return mapperService.mapResponseToResponseEntity(result)
    }
}
