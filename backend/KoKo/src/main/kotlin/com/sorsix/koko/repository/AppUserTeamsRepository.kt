package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUserTeams
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserTeamsRepository : JpaRepository<AppUserTeams, Long>