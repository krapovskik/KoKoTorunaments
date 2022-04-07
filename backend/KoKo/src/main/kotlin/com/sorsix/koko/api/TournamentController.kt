package com.sorsix.koko.api

import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.service.TournamentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tournament")
class TournamentController(val tournamentService: TournamentService) {

    @GetMapping
    fun findAllByTimeline(): Map<String, List<Tournament>> = tournamentService.findAllGroupByTimeLine();


}