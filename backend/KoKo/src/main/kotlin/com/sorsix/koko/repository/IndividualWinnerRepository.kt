package com.sorsix.koko.repository

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.IndividualWinner
import org.springframework.data.jpa.repository.JpaRepository

interface IndividualWinnerRepository : JpaRepository<IndividualWinner, Long> {
    fun findAllByAppUser(appUser: AppUser): List<IndividualWinner>
}
