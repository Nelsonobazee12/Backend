package com.example.backend.bankAccount

import com.example.backend.Entities.users.AppUser
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable

@Entity
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val message: String,
    val timestamp: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    val appUser: AppUser
) {
    override fun toString(): String {
        return "Notification(id=$id, message=$message, timestamp=$timestamp)"
    }
}
