package com.example.backend.bankAccount

import com.example.backend.Entities.users.AppUser
import jakarta.persistence.*

@Entity
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val message: String,
    val timestamp: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    val appUser: AppUser
)
