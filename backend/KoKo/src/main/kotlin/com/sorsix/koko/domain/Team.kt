package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "teams")
data class Team(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "name")
    val name: String,

    @Column(name = "is_valid")
    val isValid: Boolean = true,

)
