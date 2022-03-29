package com.sorsix.koko.domain

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
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
    private val password: String,

    @Enumerated(value = EnumType.STRING)
    val userRole: UserRole,

    @Column(name = "is_valid")
    val isValid: Boolean = false,

    ) : UserDetails {

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(userRole.name))

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = isValid
}
