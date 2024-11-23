package com.example.backend.bankAccount.transaction

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class DepositRequest(@field:NotNull @field:Positive val amount: Double?)
