package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.enumeration.AppUserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {

    fun findAppUserByEmail(email: String): AppUser?

    fun findAllByAppUserRole(pageable: Pageable, appUserRole: AppUserRole): Page<AppUser>

    @Modifying
    @Query(value = "update AppUser a set a.appUserRole = :userRole where a.id = :userId")
    fun updateAppUserRole(userId: Long, userRole: AppUserRole): Int
}
