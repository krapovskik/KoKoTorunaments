package com.sorsix.koko.repository

import com.sorsix.koko.domain.TeamTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface TeamTournamentRepository : JpaRepository<TeamTournament, Long>{

    fun findAllByTournamentId(tournamentId: Long): MutableList<TeamTournament>

    @Modifying
    @Query(value = "delete from TeamTournament t where t.tournament.id = :tournamentId")
    fun deleteTeams(tournamentId: Long): Int

}