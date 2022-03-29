package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "tournaments")
data class Tournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "name")
    val name: String,

    @Column(name = "category")
    val category: String,

    @Column(name = "number_of_participants")
    val numberOfParticipants: Int,

    @ManyToMany
    val teams: List<Team>,

    @ManyToMany
    val players: List<AppUser>

)