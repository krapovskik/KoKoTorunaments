package com.sorsix.koko.service

import com.sorsix.koko.domain.ActivationToken
import com.sorsix.koko.domain.AppUser
import com.sorsix.koko.domain.AppUserTeams
import com.sorsix.koko.domain.OrganizerRequest
import com.sorsix.koko.domain.enumeration.AppUserRole
import com.sorsix.koko.dto.request.ActivateAccountRequest
import com.sorsix.koko.dto.request.RegisterRequest
import com.sorsix.koko.dto.request.RequestOrganizerRequest
import com.sorsix.koko.dto.request.SendInviteRequest
import com.sorsix.koko.dto.response.*
import com.sorsix.koko.repository.AppUserRepository
import com.sorsix.koko.repository.AppUserTeamsRepository
import com.sorsix.koko.repository.OrganizerRequestRepository
import com.sorsix.koko.repository.TeamRepository
import com.sorsix.koko.util.EmailService
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AppUserService(
    val appUserRepository: AppUserRepository,
    val organizerRequestRepository: OrganizerRequestRepository,
    val teamRepository: TeamRepository,
    val appUserTeamsRepository: AppUserTeamsRepository,
    val passwordEncoder: PasswordEncoder,
    val activationTokenService: ActivationTokenService,
    val emailService: EmailService
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? = appUserRepository.findAppUserByEmail(username)

    fun findAppUserByIdOrNull(appUserId: Long): AppUser? = appUserRepository.findByIdOrNull(appUserId)

    @Transactional
    fun registerUser(registerRequest: RegisterRequest): Response {

        val email = registerRequest.email

        return when (val result = createUser(email)) {
            is SuccessResponse<*> -> {
                emailService.sendNewAccountMail(email, result.response.toString())
                SuccessResponse("User registered successfully. Check your email")
            }
            else -> result
        }
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
                isActive = true
            )

        this.saveUser(activatedUser)
        activationTokenService.deleteToken(activationToken)

        return SuccessResponse("Account activated successfully")
    }

    fun saveUser(appUser: AppUser) = appUserRepository.save(appUser)

    fun saveOrganizerRequest(requestOrganizer: RequestOrganizerRequest): Response {
        val appUser = SecurityContextHolder.getContext().authentication.principal as AppUser

        if (organizerRequestRepository.existsByUser(appUser)) {
            return BadRequestResponse("You have already sent a request")
        }

        val (title, description) = requestOrganizer
        organizerRequestRepository.save(OrganizerRequest(0, title, description, appUser))

        return SuccessResponse("Request sent successfully")
    }

    fun searchUser(query: String?): List<TeamMemberResponse> = query?.let {
        appUserRepository.searchAppUserByFirstNameOrLastName(query.lowercase()).map { result ->
            TeamMemberResponse("${result.firstName} ${result.lastName}-${result.id}")
        }
    } ?: listOf()

    fun sendInvite(sendInviteRequest: SendInviteRequest): Response {
        val (email, teamId) = sendInviteRequest

        return when (val result = createUser(email)) {
            is SuccessResponse<*> -> {
                val team = teamRepository.findByIdOrNull(teamId)
                team?.let {
                    val invitedBy = SecurityContextHolder.getContext().authentication.principal as AppUser
                    val activationToken = result.response as ActivationToken
                    appUserTeamsRepository.save(AppUserTeams(team = team, appUser = activationToken.user))
                    emailService.sendInviteEmail(
                        email,
                        team.name,
                        "${invitedBy.firstName} ${invitedBy.lastName}",
                        activationToken.token
                    )
                    SuccessResponse("Invitation sent successfully")
                } ?: NotFoundResponse("Team not found")
            }
            else -> result
        }
    }

    private fun createUser(email: String): Response {

        if (EmailValidator.getInstance().isValid(email)) {

            appUserRepository.findAppUserByEmail(email)?.let {
                return NotFoundResponse("Email already exists")
            }

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
            return SuccessResponse<ActivationToken>(activationToken)
        }

        return NotFoundResponse("Invalid email format")
    }
}

//TODO() invite function - send invite, create account, add player to team