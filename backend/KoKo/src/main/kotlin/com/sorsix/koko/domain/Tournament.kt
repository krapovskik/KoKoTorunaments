package com.sorsix.koko.domain

import com.sorsix.koko.domain.enumeration.TimelineTournamentType
import com.sorsix.koko.domain.enumeration.TournamentType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tournaments")
data class Tournament(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val category: String,
    val location: String,
    val description: String,
    val numberOfParticipants: Int,

    @Enumerated(value = EnumType.STRING)
    val type: TournamentType,

    @Column(name = "timeline")
    @Enumerated(value = EnumType.STRING)
    val timelineType: TimelineTournamentType,
    val dateCreated: LocalDateTime = LocalDateTime.now(),

    @Column(name = "start_date")
    val startingDate: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "organizer")
    val organizer: AppUser
)
