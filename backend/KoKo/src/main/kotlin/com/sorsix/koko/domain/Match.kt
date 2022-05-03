package com.sorsix.koko.domain

sealed interface Match<T> where T : Match<T> {
    val id: Long
    val winner: Int?
    val isFinished: Boolean
    val number: Int
    val round: Int
    val score1: Int?
    val score2: Int?
    val nextMatch: Match<T>?
    val player1: Any?
    val player2: Any?

    fun copy(
        nextMatch: Match<T>? = this.nextMatch,
        isFinished: Boolean = this.isFinished,
        player1: Any? = this.player1,
        player2: Any? = this.player2
    ): T
}
