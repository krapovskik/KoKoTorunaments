package com.sorsix.koko.service

import com.sorsix.koko.domain.Tournament
import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import com.sorsix.koko.dto.response.NotFoundResponse
import com.sorsix.koko.dto.response.Response
import com.sorsix.koko.dto.response.SuccessResponse
import com.sorsix.koko.repository.TournamentRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TournamentService(val tournamentRepository: TournamentRepository) {

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

    fun findAllGroupByTimeLine(): Map<String, List<Tournament>> =
        tournamentRepository.findAll().groupBy { it.timelineType.name }

    fun findAllByTimelineType(timelineTournamentType: TimelineTournamentType): List<Tournament> =
        tournamentRepository.findAllByTimelineType(timelineTournamentType)


}