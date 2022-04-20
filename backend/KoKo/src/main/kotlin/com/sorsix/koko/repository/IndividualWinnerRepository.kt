package com.sorsix.koko.repository

import com.sorsix.koko.domain.IndividualWinner
import org.springframework.data.jpa.repository.JpaRepository

interface IndividualWinnerRepository : JpaRepository<IndividualWinner, Long>
