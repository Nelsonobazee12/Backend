package com.example.backend.bankAccount
import com.example.backend.Entities.users.AppUser
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable


@Entity
data class BankCard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    @JsonIgnore
    val appUser: AppUser? = null,

    var balance: Double = 0.0
) {
    val cardHolderName: String
        get() = appUser?.name.toString()

    override fun toString(): String {
        return "BankCard(id=$id, cardNumber=$cardNumber, expiryDate=$expiryDate, cvv=$cvv, balance=$balance)"
    }
}

