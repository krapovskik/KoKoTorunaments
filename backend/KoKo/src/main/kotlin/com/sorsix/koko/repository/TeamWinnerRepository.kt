package com.sorsix.koko.repository

import com.sorsix.koko.domain.Team
import com.sorsix.koko.domain.TeamWinner
import com.sorsix.koko.domain.Tournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TeamWinnerRepository : JpaRepository<TeamWinner, Long> {

    fun findAllByTeamIn(teams: List<Team>): List<TeamWinner>
}
