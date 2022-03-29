package com.sorsix.koko.repository

import com.sorsix.koko.domain.ActivationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivationTokenRepository : JpaRepository<ActivationToken, String>
