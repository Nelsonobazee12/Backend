package com.example.backend.repository

import com.example.backend.Entities.users.AppUser
import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByBankCard(bankCard: BankCard?): List<Transaction>
    fun findAllByDescriptionContainingIgnoreCase(description: String): List<Transaction>
}
