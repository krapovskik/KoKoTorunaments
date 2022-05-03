package com.sorsix.koko.repository

import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TournamentRepository : JpaRepository<Tournament, Long> {

    fun findAllByTimelineTypeOrderByDateCreatedDesc(timelineTournamentType: TimelineTournamentType): List<Tournament>

    fun findAllByTimelineTypeOrderByDateCreatedDesc(
        timelineTournamentType: TimelineTournamentType, pageable: Pageable
    ): Page<Tournament>

    @Modifying
    @Query(value = "update Tournament t set t.timelineType = :timelineType where t.id = :tournamentId")
    fun updateTournamentStatus(timelineType: TimelineTournamentType, tournamentId: Long)

    fun findAllByIdIn(ids: List<Long>): List<Tournament>
}
