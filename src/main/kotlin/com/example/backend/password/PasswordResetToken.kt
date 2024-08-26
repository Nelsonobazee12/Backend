package com.example.backend.password

import com.example.backend.Entities.users.AppUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long = 0
    var otpToken : String = ""

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var appUser : AppUser? = null

    var localDateTime : LocalDateTime? = null
 }

