package com.sorsix.koko.domain

import javax.persistence.*

@Table(name = "individual_winners")
@Entity
data class IndividualWinner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @OneToOne
    @JoinColumn(name = "app_user_id")
    val appUser: AppUser,
)
