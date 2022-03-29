package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "matches")
data class Match(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "winner")
    val winner: Int?,

    @ManyToOne
    val tournament: Tournament
)
