package com.sorsix.koko.repository

import com.sorsix.koko.domain.TeamMatch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamMatchRepository : JpaRepository<TeamMatch, Long>