package com.sorsix.koko.repository.view

import com.sorsix.koko.domain.view.PlayersInTeamTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayersInTeamTournamentRepository : JpaRepository<PlayersInTeamTournament, Long>