package com.example.backend.token
import com.example.backend.Entities.users.AppUser
import jakarta.persistence.*

@Entity
data class Token(
    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(unique = true, nullable = false, name = "token_value")
    var token: String? = null,

    @Enumerated(EnumType.STRING)
    var tokenType: TokenType = TokenType.BEARER,

    var isRevoked: Boolean = false,

    var isExpired: Boolean = false,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "app_user_id", nullable = false)
    var appUser: AppUser? = null,

    @Column(name = "value")
    var value: String? = null
)

