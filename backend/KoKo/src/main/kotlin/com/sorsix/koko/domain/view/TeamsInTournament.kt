package com.sorsix.koko.domain.view

import org.hibernate.annotations.Immutable
import org.hibernate.annotations.Subselect
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Immutable
@Subselect(value = "select * from teams_in_tournament")
data class TeamsInTournament(

    @Id
    @Column(name = "team_id")
    val teamId: Long,

    @Column(name = "tournament_id")
    val tournamentId: Long,

    @Column(name = "name")
    val teamName: String

)