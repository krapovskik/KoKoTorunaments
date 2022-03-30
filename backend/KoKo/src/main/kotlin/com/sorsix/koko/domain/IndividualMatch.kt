package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "individual_matches")
data class IndividualMatch(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "winner")
    val winner: Int?,

    @OneToOne
    @JoinColumn(name = "app_user1_id")
    val player1: AppUser,

    @OneToOne
    @JoinColumn(name = "app_user2_id")
    val player2: AppUser

)
