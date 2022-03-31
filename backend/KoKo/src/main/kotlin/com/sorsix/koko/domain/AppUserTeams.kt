package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "app_user_teams")
data class AppUserTeams(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "team_id")
    val team: Team,

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    val appUser: AppUser


)
