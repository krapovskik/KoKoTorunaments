package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "activation_tokens")
data class ActivationToken(

    @Id
    @Column(name = "token")
    val token: String,

    @OneToOne
    @JoinColumn(name = "app_user_id")
    val user: AppUser

)