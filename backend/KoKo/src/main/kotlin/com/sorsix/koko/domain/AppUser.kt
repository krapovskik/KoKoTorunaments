package com.sorsix.koko.domain

import com.sorsix.koko.domain.enumeration.AppUserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "app_users")
data class AppUser(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    private val password: String,

    @Enumerated(value = EnumType.STRING)
    val appUserRole: AppUserRole,
    val isActive: Boolean = false,

    @Column(name = "profile_photo_url")
    val profilePhoto: String = "profile0.jpg"
) : UserDetails {

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(appUserRole.name))

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = isActive
}
