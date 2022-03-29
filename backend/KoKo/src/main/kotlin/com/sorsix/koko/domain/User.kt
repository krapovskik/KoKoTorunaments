package com.sorsix.koko.domain

import javax.persistence.*

data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

)
