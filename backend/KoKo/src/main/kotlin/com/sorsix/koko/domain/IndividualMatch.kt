package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "individual_matches")
data class IndividualMatch(
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
    override val nextMatch: IndividualMatch? = null,

    @OneToOne
    @JoinColumn(name = "app_user1_id")
    override val player1: AppUser? = null,

    @OneToOne
    @JoinColumn(name = "app_user2_id")
    override val player2: AppUser? = null
) : Match<IndividualMatch> {

    override fun copy(
        nextMatch: Match<IndividualMatch>?,
        isFinished: Boolean,
        player1: Any?,
        player2: Any?
    ): IndividualMatch = copy(
        nextMatch = nextMatch as IndividualMatch?,
        isFinished = isFinished,
        player1 = player1 as AppUser?,
        player2 = player2 as AppUser?
    )
}
