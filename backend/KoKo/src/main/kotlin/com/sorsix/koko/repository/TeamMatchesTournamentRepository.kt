package com.sorsix.koko.repository

import com.sorsix.koko.domain.TeamMatchesTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamMatchesTournamentRepository : JpaRepository<TeamMatchesTournament, Long>