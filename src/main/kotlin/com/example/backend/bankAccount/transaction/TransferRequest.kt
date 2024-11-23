package com.example.backend.bankAccount.transaction

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class TransferRequest(
    val destinationCardNumber: String,
    @field:NotNull @field:Positive val amount: Double,
    val description: String?
)



