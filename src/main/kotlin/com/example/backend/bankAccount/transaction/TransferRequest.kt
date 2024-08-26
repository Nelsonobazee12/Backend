package com.example.backend.bankAccount.transaction

import java.time.LocalDateTime


data class TransferRequest(
    val sourceCardNumber: String,
    val destinationCardNumber: String,
    val amount: Double,
    val description: String?
)


