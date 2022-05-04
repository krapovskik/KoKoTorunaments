package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.AppUserTeams
import com.sorsix.koko.domain.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AppUserTeamsRepository : JpaRepository<AppUserTeams, Long> {

    fun existsByAppUserAndTeam(appUser: AppUser, team: Team): Boolean

    fun findAppUserTeamsByTeam(team: Team): List<AppUserTeams>

    fun findAppUserTeamsByAppUser(appUser: AppUser): List<AppUserTeams>

    @Query(
        value = """select t1 from AppUserTeams t1 where t1.team.id in
                        (select t2.team.id from AppUserTeams t2 where t2.appUser = :appUser)"""
    )
    fun findTeamsForUser(appUser: AppUser): List<AppUserTeams>

    @Modifying
    @Query(value = "delete from AppUserTeams a where a.appUser.id in :userIds")
    fun deleteAllByAppUserIn(userIds: List<Long>)
}
