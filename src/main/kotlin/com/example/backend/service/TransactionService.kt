package com.example.backend.service

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.Transaction
import com.example.backend.repository.BankCardRepository
import com.example.backend.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val bankCardRepository: BankCardRepository
) {

    fun createTransaction(bankCard: BankCard,
                          amount: Double,
                          type: String,
                          balanceAfterTransaction: Double,
                          description: String? = null
    ): Transaction {
        // Adjusting balance based on transaction type
//        when (type) {
//            "DEPOSIT" -> bankCard.balance += amount
//            "WITHDRAWAL", "TRANSFER", "PAYING_OF_BILLS" -> bankCard.balance -= amount
//            else -> throw IllegalArgumentException("Invalid transaction type")
//        }

        val transaction = Transaction(
            amount = amount,
            type = type,
            timestamp = LocalDateTime.now().toString(),
            description = description,
            cardNumber = bankCard.cardNumber,
            cardHolderName = bankCard.cardHolderName,
            balanceAfterTransaction = balanceAfterTransaction, // Store balance after the transaction
            bankCard = bankCard
        )

//        // Save updated balance to the BankCard entity
//        bankCardRepository.save(bankCard)

        return transactionRepository.save(transaction)
    }

    fun getTransactionHistory(bankCard: BankCard): List<Transaction> {
        return transactionRepository.findByBankCard(bankCard)
    }
}


