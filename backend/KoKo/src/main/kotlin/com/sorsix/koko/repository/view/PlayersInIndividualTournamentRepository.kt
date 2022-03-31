package com.sorsix.koko.repository.view

import com.sorsix.koko.domain.view.PlayersInIndividualTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayersInIndividualTournamentRepository : JpaRepository<PlayersInIndividualTournament, Long>