package com.sorsix.koko.repository

import com.sorsix.koko.domain.TeamMatch
import com.sorsix.koko.domain.TeamMatchesTournament
import com.sorsix.koko.domain.Tournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TeamMatchesTournamentRepository : JpaRepository<TeamMatchesTournament, Long> {

    @Query(value = "select tm.teamMatch from TeamMatchesTournament tm where tm.tournament=:tournament")
    fun findAllMatchesByTournament(tournament: Tournament): List<TeamMatch>
}
