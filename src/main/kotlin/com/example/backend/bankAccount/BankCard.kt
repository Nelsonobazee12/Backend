package com.example.backend.bankAccount


import com.example.backend.Entities.users.AppUser
import jakarta.persistence.*


@Entity
data class BankCard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val cardNumber: String?,
    val expiryDate: String?,
    val cvv: String?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    val appUser: AppUser,

    var balance: Double = 0.0
) {
    val cardHolderName: String
        get() = appUser.name.toString()
}

