package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "team_tournaments")
data class TeamTournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @ManyToOne
    @JoinColumn(name = "team_id")
    val team: Team

)