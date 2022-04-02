package com.sorsix.koko.repository

import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.domain.enumeration.TournamentType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository : JpaRepository<Tournament, Long> {

    @Modifying
    @Query(value = "update Tournament t set " +
            "t.name = :tournamentName, " +
            "t.category = :tournamentCategory, " +
            "t.numberOfParticipants = :numberOfParticipants," +
            "t.type = :tournamentType " +
            "where t.id = :tournamentId"
    )
    fun updateTournament(
        tournamentId: Long,
        tournamentName: String,
        tournamentCategory: String,
        numberOfParticipants: Int,
        tournamentType: TournamentType
    ): Int

    @Modifying
    @Query(value = "delete from Tournament t where t.id = :tournamentId")
    fun deleteTournament(tournamentId: Long): Int

}