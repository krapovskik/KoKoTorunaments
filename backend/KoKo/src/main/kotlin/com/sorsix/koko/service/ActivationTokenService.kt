package com.sorsix.koko.service

import com.sorsix.koko.domain.ActivationToken
import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.repository.ActivationTokenRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ActivationTokenService(val activationTokenRepository: ActivationTokenRepository) {

    fun createTokenForUser(appUser: AppUser): ActivationToken {
        val token = UUID.randomUUID().toString()
        return activationTokenRepository.save(ActivationToken(token, appUser))
    }

    fun saveToken(activationToken: ActivationToken) = activationTokenRepository.save(activationToken)

    fun getToken(token: String): ActivationToken? = activationTokenRepository.findByIdOrNull(token)

    fun deleteToken(activationToken: ActivationToken) = activationTokenRepository.delete(activationToken)

    fun deleteAllTokens() = activationTokenRepository.deleteAll()
}
