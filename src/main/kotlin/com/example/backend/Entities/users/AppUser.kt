package com.example.backend.Entities.users

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.Notification
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


    @JoinColumn(name = "image_id")
    var profileImage: String? = "http://res.cloudinary.com/dbjwj3ugv/image/upload/v1724239879/salv2gaw1mydiwscxyay.png",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var roles : Role?,

    @Column(nullable = false)
    private var enabled: Boolean,

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var tokens: List<Token>? = mutableListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val bankCards: List<BankCard> = mutableListOf(),

    @JsonIgnore
    @OneToMany(mappedBy = "appUser", cascade = [CascadeType.REMOVE])
    var notifications: List<Notification> = mutableListOf(),
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "appUser", cascade = [CascadeType.REMOVE])
//    var transaction : List<Transaction> = mutableListOf()

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