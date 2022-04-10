package com.sorsix.koko.repository

import com.sorsix.koko.domain.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : JpaRepository<Team,Long>{

    fun existsByName(name: String): Boolean

    @Modifying
    @Query(value = "update Team t set t.name = :teamName where t.id = :teamId ")
    fun updateTeamName(teamId: Long, teamName: String): Int

    @Modifying
    @Query(value = "update Team t set t.isActive = :teamStatus where t.id = :teamId ")
    fun updateTeamStatus(teamId: Long, teamStatus: Boolean): Int

}