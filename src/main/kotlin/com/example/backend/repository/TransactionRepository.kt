package com.example.backend.repository

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByBankCard(bankCard: BankCard): List<Transaction>
}
