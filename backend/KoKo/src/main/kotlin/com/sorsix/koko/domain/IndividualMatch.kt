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
    val number: Int? = null,

    @Column(name = "round")
    val round: Int? = null,

    @OneToOne
    @JoinColumn(name = "app_user1_id")
    val player1: AppUser? = null,

    @OneToOne
    @JoinColumn(name = "app_user2_id")
    val player2: AppUser? = null
)
