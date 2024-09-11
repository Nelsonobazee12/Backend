package com.example.backend.token

import com.example.backend.Entities.users.AppUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "verification_tokens")
class VerificationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: AppUser,

    @Column(nullable = false)
    val expirationTime: LocalDateTime
)


