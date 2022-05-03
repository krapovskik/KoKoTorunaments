package com.sorsix.koko.repository

import com.sorsix.koko.domain.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<Team, Long> {

    fun existsByName(name: String): Boolean
}
