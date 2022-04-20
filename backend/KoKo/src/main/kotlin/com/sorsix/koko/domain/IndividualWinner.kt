package com.sorsix.koko.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "individual_winners")
@Entity
data class IndividualWinner(
    @Id
    @Column(name = "id")
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "tournament_id")
    val tournament: Tournament,

    @OneToOne
    @JoinColumn(name = "app_user_id")
    val appUser: AppUser,
)
