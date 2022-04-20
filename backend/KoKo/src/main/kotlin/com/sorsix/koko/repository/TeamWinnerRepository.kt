package com.sorsix.koko.repository

import com.sorsix.koko.domain.TeamWinner
import org.springframework.data.jpa.repository.JpaRepository

interface TeamWinnerRepository : JpaRepository<TeamWinner, Long>
