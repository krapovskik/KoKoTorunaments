package com.sorsix.koko.domain.view

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Subselect
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Immutable
@Subselect(value = "select * from players_in_individual_tournament")
data class PlayersInIndividualTournament(

    @Id
    @Column(name = "app_user_id")
    val playerId: Long,

    @Column(name = "tournament_id")
    val tournamentId: Long,

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String

)