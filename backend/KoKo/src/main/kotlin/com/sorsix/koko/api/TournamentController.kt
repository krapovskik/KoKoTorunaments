package com.sorsix.koko.api

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.dto.response.TournamentResponse
import com.sorsix.koko.service.TournamentService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tournament")
class TournamentController(val tournamentService: TournamentService) {

    @GetMapping
    fun findAllGrouped(): Map<String, List<TournamentResponse>> = tournamentService.findAllGroupByTimeLine();

    @GetMapping("/ongoing")
    fun getOngoingTournaments(pageable: Pageable) =
        tournamentService.getAllTournamentsByTimelinePaginated(pageable, TimelineTournamentType.ONGOING)

    @GetMapping("/finished")
    fun getFinishedTournaments(pageable: Pageable) =
        tournamentService.getAllTournamentsByTimelinePaginated(pageable, TimelineTournamentType.FINISHED)

    @GetMapping("/comingSoon")
    fun getComingSoonTournaments(pageable: Pageable) =
        tournamentService.getAllTournamentsByTimelinePaginated(pageable, TimelineTournamentType.COMING_SOON)
}