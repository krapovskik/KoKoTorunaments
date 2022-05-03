package com.sorsix.koko.repository

import com.sorsix.koko.domain.IndividualTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IndividualTournamentRepository : JpaRepository<IndividualTournament, Long> {

    fun findAllByTournamentId(tournamentId: Long): MutableList<IndividualTournament>

    @Modifying
    @Query(value = "delete from IndividualTournament t where t.tournament.id = :tournamentId")
    fun deleteAppUsers(tournamentId: Long): Int
}
