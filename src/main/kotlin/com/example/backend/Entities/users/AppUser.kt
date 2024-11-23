package com.example.backend.Entities.users

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.Notification
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "app_user")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
data class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val auth0Id: String?,

    @Column(nullable = false)
    var name: String?,

    @Column(nullable = false, unique = true)
    var email: String?,

    @Column(nullable = false)
    private var password: String?,

    @Column(nullable = false)
    var profileImage: String = "https://res.cloudinary.com/dbjwj3ugv/image/upload/v1725105327/fjmlxipfxhaltwfnlxsc.png",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var roles: Role,

    @Column(nullable = false)
    var enabled: Boolean,

//    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
//    @JsonIgnore
//    var tokens: List<Token> = mutableListOf(),

    @OneToOne(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    var bankCard: BankCard? = null,

    @OneToMany(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    var notifications: List<Notification> = mutableListOf(),

    var isFirstLogin: Boolean = true,
    var isTwoFactorAuthEnabled: Boolean,
    var secret: String? = null
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles?.getAuthorities() ?: emptyList()
    }

    override fun getPassword(): String {
        return password ?: ""
    }

    override fun getUsername(): String {
        return email ?: ""
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    fun updatePassword(newPassword: String): AppUser {
        return this.copy(password = newPassword)
    }

    // Optional: Avoid recursive toString if needed
    override fun toString(): String {
        return "AppUser(id=$id, name=$name, email=$email, enabled=$enabled, isFirstLogin=$isFirstLogin)"
    }
}
