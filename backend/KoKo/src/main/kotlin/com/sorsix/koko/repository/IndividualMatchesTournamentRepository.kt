package com.sorsix.koko.repository

import com.sorsix.koko.domain.IndividualMatch
import com.sorsix.koko.domain.IndividualMatchesTournament
import com.sorsix.koko.domain.Tournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IndividualMatchesTournamentRepository : JpaRepository<IndividualMatchesTournament, Long> {

    @Query(value = "select im.individualMatch from IndividualMatchesTournament im where im.tournament=:tournament")
    fun findAllMatchesByTournament(tournament: Tournament): List<IndividualMatch>
}
