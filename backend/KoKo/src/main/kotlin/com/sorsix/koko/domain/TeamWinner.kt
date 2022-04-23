package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "team_winners")
data class TeamWinner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @OneToOne
    @JoinColumn(name = "team_id")
    val team: Team,
)
