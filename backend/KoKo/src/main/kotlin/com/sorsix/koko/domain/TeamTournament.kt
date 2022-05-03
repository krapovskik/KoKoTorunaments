package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "team_tournaments")
data class TeamTournament(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @ManyToOne
    @JoinColumn(name = "team_id")
    override val player: Team
): TypedTournament
