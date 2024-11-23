package com.example.backend.service

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.Transaction
import com.example.backend.repository.BankCardRepository
import com.example.backend.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val bankCardRepository: BankCardRepository
) {

    /**
     * Creates a transaction and updates the bank card balance.
     * @param bankCard the bank card associated with the transaction.
     * @param amount the amount of the transaction.
     * @param type the type of the transaction (e.g., DEPOSIT, WITHDRAWAL).
     * @param balanceAfterTransaction the balance after the transaction.
     * @param description optional description for the transaction.
     * @return the created Transaction instance.
     */
    @Transactional
//    @CacheEvict(value = ["transactions"], key = "#bankCard.cardNumber")
    fun createTransaction(
        bankCard: BankCard,
        amount: Double,
        type: String,
        balanceAfterTransaction: Double,
        description: String? = null
    ): Transaction {

        val transaction = Transaction(
            amount = amount,
            type = type,
            timestamp = LocalDateTime.now().toString(),
            description = description,
            cardNumber = bankCard.cardNumber,
            cardHolderName = bankCard.cardHolderName,
            balanceAfterTransaction = balanceAfterTransaction,
            bankCard = bankCard
        )

        return transactionRepository.save(transaction)
    }


    /**
     * Retrieves the transaction history for a given bank card.
     * @param bankCard the bank card whose transaction history is to be retrieved.
     * @return a list of Transactions associated with the given bank card.
     */
//    @Cacheable(value = ["transactions"], key = "#bankCard.cardNumber")
    fun getTransactionHistory(bankCard: BankCard): List<Transaction> {
        return transactionRepository.findByBankCard(bankCard)
            .sortedByDescending { it.timestamp }
    }
}



