package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "team_matches")
data class TeamMatch(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "winner")
    val winner: Int? = null,

    @OneToOne
    @JoinColumn(name = "team1_id")
    val team1: Team,

    @OneToOne
    @JoinColumn(name = "team2_id")
    val team2: Team,

)