package com.sorsix.koko.repository

import com.sorsix.koko.domain.IndividualMatch
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IndividualMatchRepository : JpaRepository<IndividualMatch, Long>