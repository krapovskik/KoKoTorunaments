package com.sorsix.koko.service

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.enumeration.AppUserRole
import com.sorsix.koko.dto.request.ActivateAccountRequest
import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.repository.AppUserRepository
import com.sorsix.koko.util.EmailService
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AppUserService(
    val appUserRepository: AppUserRepository,
    val passwordEncoder: PasswordEncoder,
    val activationTokenService: ActivationTokenService,
    val emailService: EmailService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails = appUserRepository.findAppUserByEmail(username)

    fun findAppUserByIdOrNull(appUserId: Long): AppUser? = appUserRepository.findByIdOrNull(appUserId)

    @Transactional
    fun registerUser(registerRequest: RegisterRequest): Response {

        val email = registerRequest.email

        if (EmailValidator.getInstance().isValid(email)) {
            val appUser = AppUser(
                0,
                "",
                "",
                email,
                "",
                AppUserRole.PLAYER,
                false
            )

            this.saveUser(appUser)

            val activationToken = activationTokenService.createTokenForUser(appUser)
            emailService.sendNewAccountMail(email, activationToken.token)

            return SuccessResponse("User registered successfully")
        }

        return NotFoundResponse("Invalid email format")
    }

    @Transactional
    fun activateAccount(activateAccountRequest: ActivateAccountRequest): Response {
        val (token, firstName, lastName, password) = activateAccountRequest
        val activationToken = activationTokenService.getToken(token)

        val appUser = activationToken?.user ?: return NotFoundResponse("Token doesn't exists or is expired")

        val activatedUser =
            appUser.copy(
                firstName = firstName,
                lastName = lastName,
                password = passwordEncoder.encode(password),
                isValid = true
            )

        this.saveUser(activatedUser)
        activationTokenService.deleteToken(activationToken)

        return SuccessResponse("Account activated successfully")
    }

    fun saveUser(appUser: AppUser) = appUserRepository.save(appUser)


}
