package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUserTournament
import com.sorsix.koko.domain.TeamTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AppUserTournamentRepository : JpaRepository<AppUserTournament, Long> {

    fun findAllByTournamentId(tournamentId: Long): MutableList<AppUserTournament>

    @Modifying
    @Query(value = "delete from AppUserTournament t where t.tournament.id = :tournamentId")
    fun deleteAppUsers(tournamentId: Long): Int

}