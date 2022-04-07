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
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "name")
    val name: String,

    @Column(name = "category")
    val category: String,

    @Column(name = "number_of_participants")
    val numberOfParticipants: Int,

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    val type: TournamentType,

    @Column(name = "timeline")
    @Enumerated(value = EnumType.STRING)
    val timelineType: TimelineTournamentType,

    @Column(name = "date_created")
    val dateCreated: LocalDateTime = LocalDateTime.now()

)
