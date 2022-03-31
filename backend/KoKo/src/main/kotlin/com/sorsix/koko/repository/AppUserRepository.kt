package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {

    fun findAppUserByEmail(email: String): AppUser?
}
