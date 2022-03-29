package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "app_users")
data class AppUser(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "password")
    val password: String,

    @Column(name = "is_valid")
    val isValid: Boolean = false

)