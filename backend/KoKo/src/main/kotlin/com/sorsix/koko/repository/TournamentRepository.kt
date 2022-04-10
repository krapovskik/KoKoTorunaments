package com.sorsix.koko.repository

import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository : JpaRepository<Tournament, Long> {

    fun findAllByTimelineTypeOrderByDateCreatedDesc(timelineTournamentType: TimelineTournamentType): List<Tournament>

    fun findAllByTimelineTypeOrderByDateCreatedDesc(timelineTournamentType: TimelineTournamentType, pageable: Pageable): Page<Tournament>


    @Modifying
    @Query(
        value = "update Tournament t set " +
                "t.name = :tournamentName, " +
                "t.category = :tournamentCategory, " +
                "t.numberOfParticipants = :numberOfParticipants, " +
                "t.type = :tournamentType, " +
                "t.timelineType = :tournamentTimelineType " +
                "where t.id = :tournamentId"
    )
    fun updateTournament(
        tournamentId: Long,
        tournamentName: String,
        tournamentCategory: String,
        numberOfParticipants: Int,
        tournamentType: TournamentType,
        tournamentTimelineType: TimelineTournamentType
    ): Int

    @Modifying
    @Query(value = "delete from Tournament t where t.id = :tournamentId")
    fun deleteTournament(tournamentId: Long): Int

}