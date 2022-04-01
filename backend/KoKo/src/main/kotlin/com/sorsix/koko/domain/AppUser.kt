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

    @Column(name = "app_user_role")
    @Enumerated(value = EnumType.STRING)
    val appUserRole: AppUserRole,

    @Column(name = "is_active")
    val isActive: Boolean = false,

    ) : UserDetails {

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(appUserRole.name))

    override fun getPassword() = password

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = isActive
}
