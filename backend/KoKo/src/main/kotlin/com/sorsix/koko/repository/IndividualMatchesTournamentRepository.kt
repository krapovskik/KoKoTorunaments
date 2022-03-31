package com.sorsix.koko.repository

import com.sorsix.koko.domain.IndividualMatchesTournament
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IndividualMatchesTournamentRepository : JpaRepository<IndividualMatchesTournament, Long>