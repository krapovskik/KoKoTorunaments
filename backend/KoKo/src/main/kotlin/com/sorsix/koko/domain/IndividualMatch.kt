package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "individual_matches")
data class IndividualMatch(

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
    val nextMatch: IndividualMatch? = null,

    @OneToOne
    @JoinColumn(name = "app_user1_id")
    val player1: AppUser? = null,

    @OneToOne
    @JoinColumn(name = "app_user2_id")
    val player2: AppUser? = null
)
