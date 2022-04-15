package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "app_user_tournaments")
data class AppUserTournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    val appUser: AppUser

)