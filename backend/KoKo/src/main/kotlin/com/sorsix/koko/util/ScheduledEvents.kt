package com.sorsix.koko.util

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.repository.*
import com.sorsix.koko.service.TournamentService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.LocalDate
import javax.transaction.Transactional

@Configuration
@EnableScheduling
class ScheduledEvents(
    val activationTokenRepository: ActivationTokenRepository,
    val userRepository: AppUserRepository,
    val appUserTeamsRepository: AppUserTeamsRepository,
    val tournamentRepository: TournamentRepository,
    val teamTournamentRepository: TeamTournamentRepository,
    val individualTournamentRepository: IndividualTournamentRepository,
    val tournamentService: TournamentService,
) {

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    fun deleteAllInactiveAccounts() {
        val tokens = activationTokenRepository.findAll()
        val userIds = tokens.map {
            it.user.id
        }

        appUserTeamsRepository
        activationTokenRepository.deleteAll()
        appUserTeamsRepository.deleteAllByAppUserIn(userIds)
        userRepository.deleteAllById(userIds)
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun startTournaments() = tournamentRepository
        .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.COMING_SOON)
        .filter { tournament -> tournament.startingDate.toLocalDate() == LocalDate.now() }
        .forEach { tournament ->
            when (tournament.type) {
                TournamentType.TEAM -> {
                    val participants = teamTournamentRepository.findAllByTournamentId(tournament.id)
                    tournamentService.generateTeamMatches(tournament, participants)
                }
                TournamentType.INDIVIDUAL -> {
                    val participants = individualTournamentRepository.findAllByTournamentId(tournament.id)
                    tournamentService.generateIndividualMatches(tournament, participants)
                }
            }
        }
}