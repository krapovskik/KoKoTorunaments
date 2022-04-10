package com.sorsix.koko.service

import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.domain.view.PlayersInIndividualTournament
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.dto.response.TournamentResponse
import com.sorsix.koko.repository.TournamentRepository
import com.sorsix.koko.repository.view.PlayersInIndividualTournamentRepository
import com.sorsix.koko.repository.view.TeamsInTournamentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TournamentService(
    val tournamentRepository: TournamentRepository,
    val playersInIndividualTournamentRepository: PlayersInIndividualTournamentRepository,
    val teamsInTournamentRepository: TeamsInTournamentRepository
) {

    fun findAll(tournamentId: Long): List<Tournament> = tournamentRepository.findAll()

    fun createTournament(
        tournamentName: String,
        tournamentCategory: String,
        numberOfParticipants: Int,
        tournamentType: TournamentType,
        tournamentTimelineType: TimelineTournamentType
    ): Response = SuccessResponse(
        tournamentRepository.save(
            Tournament(
                name = tournamentName,
                category = tournamentCategory,
                numberOfParticipants = numberOfParticipants,
                type = tournamentType,
                timelineType = tournamentTimelineType
            )
        )
    )

    @Transactional
    fun updateTournament(
        tournamentId: Long,
        tournamentName: String,
        tournamentCategory: String,
        numberOfParticipants: Int,
        tournamentType: TournamentType,
        tournamentTimelineType: TimelineTournamentType
    ): Response =
        if (tournamentRepository.updateTournament(
                tournamentId,
                tournamentName,
                tournamentCategory,
                numberOfParticipants,
                tournamentType,
                tournamentTimelineType
            ) == 1
        ) {
            SuccessResponse("Successfully updated.")
        } else {
            NotFoundResponse("Tournament with $tournamentId not found.")
        }

    @Transactional
    fun deleteTournament(tournamentId: Long): Response =
        if (tournamentRepository.deleteTournament(tournamentId) == 1) {
            SuccessResponse("Successfully deleted.")
        } else {
            NotFoundResponse("Tournament with $tournamentId not found.")
        }

    fun findAllGroupByTimeLine(latest: Int = 3): Map<String, List<TournamentResponse>> {
        val ongoing =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.ONGOING)
                .take(latest)
                .map { mapTournamentsWithParticipantsNumber(it) }
        val finished =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.FINISHED)
                .take(latest)
                .map { mapTournamentsWithParticipantsNumber(it) }
        val comingSoon =
            tournamentRepository
                .findAllByTimelineTypeOrderByDateCreatedDesc(TimelineTournamentType.COMING_SOON)
                .take(latest)
                .map { mapTournamentsWithParticipantsNumber(it) }

        return mapOf(
            "ONGOING" to ongoing,
            "FINISHED" to finished,
            "COMING_SOON" to comingSoon
        )
    }

    fun getAllTournamentsByTimelinePaginated(
        pageable: Pageable,
        timelineTournamentType: TimelineTournamentType
    ): Page<TournamentResponse> {
        val tournaments =
            tournamentRepository.findAllByTimelineTypeOrderByDateCreatedDesc(timelineTournamentType, pageable)

        val list = tournaments.content
            .map { mapTournamentsWithParticipantsNumber(it) }

        return PageImpl(list, pageable, tournaments.totalElements)
    }

    private fun mapTournamentsWithParticipantsNumber(tournament: Tournament): TournamentResponse =
        when (tournament.type) {
            TournamentType.INDIVIDUAL -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                playersInIndividualTournamentRepository.findAllByTournamentId(tournament.id).size,
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name
            )
            TournamentType.TEAM -> TournamentResponse(
                tournament.id,
                tournament.name,
                tournament.category,
                teamsInTournamentRepository.findAllByTournamentId(tournament.id).size,
                tournament.numberOfParticipants,
                tournament.type.name,
                tournament.timelineType.name
            )
        }

}