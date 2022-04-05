package com.sorsix.koko.repository

import com.sorsix.koko.domain.OrganizerRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizerRequestRepository : JpaRepository<OrganizerRequest, Long>
