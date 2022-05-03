package com.sorsix.koko.domain

import javax.persistence.*

@Entity
@Table(name = "organizer_requests")
data class OrganizerRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val title: String,
    val description: String,

    @OneToOne
    @JoinColumn(name = "app_user_id")
    val user: AppUser
)
