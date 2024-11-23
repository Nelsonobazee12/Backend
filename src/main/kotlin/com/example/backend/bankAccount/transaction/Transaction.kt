package com.example.backend.bankAccount.transaction

import com.example.backend.bankAccount.BankCard
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable
import java.util.UUID

@Entity
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val transactionId: String = UUID.randomUUID().toString(),
    val amount: Double,
    val type: String, // e.g., "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    val timestamp: String,
    val description: String? = null,
    // Snapshot fields
    val cardNumber: String?,
    val cardHolderName: String,
    val balanceAfterTransaction: Double,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_card_id")
    @JsonIgnore
    val bankCard: BankCard

) {

    override fun toString(): String {
        return "Transaction(id=$id, transactionId=$transactionId, amount=$amount, type=$type, timestamp=$timestamp, description=$description, cardNumber=$cardNumber, cardHolderName=$cardHolderName, balanceAfterTransaction=$balanceAfterTransaction)"
    }
}

