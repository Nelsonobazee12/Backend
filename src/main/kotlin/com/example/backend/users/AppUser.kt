package com.example.backend.users

import com.example.backend.token.Token
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


@Entity
@Table(name = "app_user")
class AppUser(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = 0,

    @Column(nullable = false)
    var name : String?,

    @Column(nullable = false)
    var email : String?,

    @Column(nullable = false)
    private var password : String?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var roles : Role?,

    @Column(nullable = false)
    private var enabled: Boolean,

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var tokens: List<Token>? = mutableListOf(),


    ): UserDetails {

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

}