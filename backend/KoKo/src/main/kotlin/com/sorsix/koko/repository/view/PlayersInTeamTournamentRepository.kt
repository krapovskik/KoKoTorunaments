package com.sorsix.koko.repository.view

import com.sorsix.koko.domain.view.PlayersInTeamTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PlayersInTeamTournamentRepository : JpaRepository<PlayersInTeamTournament, Long> {

    @Query(
        value = "select distinct tournament_id from players_in_team_tournament where app_user_id=:id",
        nativeQuery = true
    )
    fun findAllByPlayerId(id: Long): List<Long>
}