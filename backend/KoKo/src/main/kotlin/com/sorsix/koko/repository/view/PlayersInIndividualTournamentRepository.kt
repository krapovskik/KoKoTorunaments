package com.sorsix.koko.repository.view

import com.sorsix.koko.domain.view.PlayersInIndividualTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PlayersInIndividualTournamentRepository : JpaRepository<PlayersInIndividualTournament, Long> {

    fun findAllByTournamentId(id: Long): List<PlayersInIndividualTournament>

    @Query(
        value = "select distinct tournament_id from players_in_individual_tournament where app_user_id=:id",
        nativeQuery = true
    )
    fun findAllByPlayerId(id: Long): List<Long>
}
