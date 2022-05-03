package com.sorsix.koko.repository.view

import com.sorsix.koko.domain.view.TeamsInTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamsInTournamentRepository : JpaRepository<TeamsInTournament, Long>{

    fun findAllByTournamentId(id: Long): List<TeamsInTournament>
}
