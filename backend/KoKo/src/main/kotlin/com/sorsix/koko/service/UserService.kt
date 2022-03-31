package com.sorsix.koko.service

import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.UserRole
import com.sorsix.koko.dto.request.ActivateAccountRequest
import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.dto.response.ErrorResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.repository.UserRepository
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val activationTokenService: ActivationTokenService,
    val emailService: EmailService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? = userRepository.findAppUserByEmail(username)

    @Transactional
    fun registerUser(registerRequest: RegisterRequest): Response {

        val email = registerRequest.email

        if (EmailValidator.getInstance().isValid(email)) {

            userRepository.findAppUserByEmail(email)?.let {
                return ErrorResponse("Email already exists")
            }

            val appUser = AppUser(
                0,
                "",
                "",
                email,
                "",
                UserRole.PLAYER,
                false
            )

            this.saveUser(appUser)

            val activationToken = activationTokenService.createTokenForUser(appUser)
            emailService.sendNewAccountMail(email, activationToken.token)

            return SuccessResponse("User registered successfully")
        }

        return ErrorResponse("Invalid email format")
    }

    @Transactional
    fun activateAccount(activateAccountRequest: ActivateAccountRequest): Response {
        val (token, firstName, lastName, password) = activateAccountRequest
        val activationToken = activationTokenService.getToken(token)

        val appUser = activationToken?.user ?: return ErrorResponse("Token doesn't exists or is expired")

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

    fun saveUser(appUser: AppUser) = userRepository.save(appUser)
}
