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

    @Column(name = "is_finished")
    val isFinished: Boolean = false,

    @Column(name = "number")
    val number: Int,

    @Column(name = "round")
    val round: Int,

    @Column(name = "score1")
    val score1: Int? = null,

    @Column(name = "score2")
    val score2: Int? = null,

    @OneToOne
    @JoinColumn(name = "next_match")
    val nextMatch: TeamMatch? = null,

    @OneToOne
    @JoinColumn(name = "team1_id")
    val team1: Team? = null,

    @OneToOne
    @JoinColumn(name = "team2_id")
    val team2: Team? = null,
)
