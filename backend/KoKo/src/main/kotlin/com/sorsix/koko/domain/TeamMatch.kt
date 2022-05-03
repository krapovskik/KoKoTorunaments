package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "team_matches")
data class TeamMatch(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = 0L,
    override val winner: Int? = null,
    override val isFinished: Boolean = false,
    override val number: Int,
    override val round: Int,
    override val score1: Int? = null,
    override val score2: Int? = null,

    @OneToOne
    @JoinColumn(name = "next_match")
    override val nextMatch: TeamMatch? = null,

    @OneToOne
    @JoinColumn(name = "team1_id")
    override val player1: Team? = null,

    @OneToOne
    @JoinColumn(name = "team2_id")
    override val player2: Team? = null,
) : Match<TeamMatch> {

    override fun copy(
        nextMatch: Match<TeamMatch>?,
        isFinished: Boolean,
        player1: Any?,
        player2: Any?
    ): TeamMatch = copy(
        nextMatch = nextMatch as TeamMatch?,
        isFinished = isFinished,
        player1 = player1 as Team?,
        player2 = player2 as Team?
    )
}
