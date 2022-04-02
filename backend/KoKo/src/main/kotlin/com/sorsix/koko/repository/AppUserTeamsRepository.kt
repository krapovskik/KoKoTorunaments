package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.AppUserTeams
import com.sorsix.koko.domain.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserTeamsRepository : JpaRepository<AppUserTeams, Long> {

    fun findAppUserTeamsByTeam(team: Team): List<AppUserTeams>

    fun findAppUserTeamsByAppUser(appUser: AppUser): List<AppUserTeams>

}