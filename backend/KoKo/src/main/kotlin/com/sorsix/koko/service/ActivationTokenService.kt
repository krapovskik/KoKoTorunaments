package com.sorsix.koko.service

import com.sorsix.koko.domain.ActivationToken
import com.sorsix.koko.repository.ActivationTokenRepository
import org.springframework.stereotype.Service

@Service
class ActivationTokenService(val activationTokenRepository: ActivationTokenRepository) {

    fun saveToken(activationToken: ActivationToken) = activationTokenRepository.save(activationToken)

    fun getToken(token: String) = activationTokenRepository.findById(token)

    fun deleteToken(activationToken: ActivationToken) = activationTokenRepository.delete(activationToken)

    fun deleteAllTokens() = activationTokenRepository.deleteAll()
}
