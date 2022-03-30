package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "team_matches_tournament")
data class TeamMatchesTournament(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @ManyToOne
    @JoinColumn(name = "team_match_id")
    val teamMatch: TeamMatch

)