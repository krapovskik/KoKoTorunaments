package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "individual_tournaments")
data class IndividualTournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    override val player: AppUser
): TypedTournament
