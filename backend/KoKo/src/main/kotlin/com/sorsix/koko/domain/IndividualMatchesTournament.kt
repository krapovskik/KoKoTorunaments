package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "individual_matches_tournaments")
data class IndividualMatchesTournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @ManyToOne
    @JoinColumn(name = "individual_match_id")
    val individualMatch: IndividualMatch
)
