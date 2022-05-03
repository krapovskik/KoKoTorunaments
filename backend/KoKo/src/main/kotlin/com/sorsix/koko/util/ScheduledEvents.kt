package com.sorsix.koko.util

import com.sorsix.koko.repository.ActivationTokenRepository
import com.sorsix.koko.repository.AppUserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledEvents(
    val activationTokenRepository: ActivationTokenRepository,
    val userRepository: AppUserRepository
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteAllInactiveAccounts() {
        val tokens = activationTokenRepository.findAll()
        val userIds = tokens.map {
            it.user.id
        }

        activationTokenRepository.deleteAll()
        userRepository.deleteAllById(userIds)
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun startTournaments() {

    }
}