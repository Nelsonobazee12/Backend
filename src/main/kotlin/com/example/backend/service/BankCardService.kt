package com.example.backend.service

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.TransferRequest
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.BankCardRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Service
class BankCardService(
    private val bankCardRepository: BankCardRepository,
    private val appUserRepository: AppUserRepository,
    private val transactionService: TransactionService,
    private val notificationService: NotificationService
) {

    @Transactional
    fun createBankCard(userId: Long, initialBalance: Double = 0.0): BankCard {
        val appUser = appUserRepository.findById(userId).orElseThrow { Exception("User not found") }

        // Check if the user already has a bank card
        val existingCard = bankCardRepository.findByAppUser(appUser)

        if (existingCard != null) {
            // If a bank card already exists for the user, throw an exception
            throw Exception("User already has a bank card")
        }

        // Create a new bank card for the user
        val bankCard = BankCard(
            cardNumber = generateCardNumber(), // Or use the provided cardNumber if required
            expiryDate = generateExpiryDate(),
            cvv = generateCvv(),
            appUser = appUser,
            balance = initialBalance
        )
        bankCardRepository.save(bankCard)

        // Create a notification for the successful creation of the bank card
        notificationService.createNotification("Bank Card Created successfully", bankCard.appUser)

        return bankCard
    }


    @Transactional
    fun addFunds(cardId: Long, amount: Double): BankCard {
        val bankCard = bankCardRepository.findById(cardId).orElseThrow { Exception("Bank card not found") }
        bankCard.balance += amount
        bankCardRepository.save(bankCard)

        // Record transaction with the updated balance
        transactionService.createTransaction(
            bankCard = bankCard,
            amount = amount,
            type = "DEPOSIT",
            description = null, // or provide a description if needed
            balanceAfterTransaction = bankCard.balance
        )

        //send notification
        notificationService.createNotification("Deposit of $$amount successful", bankCard.appUser)

        return bankCard
    }

    @Transactional
    fun deductFunds(cardId: Long, amount: Double): BankCard {
        val bankCard = bankCardRepository.findById(cardId).orElseThrow { Exception("Bank card not found") }
        if (bankCard.balance >= amount) {
            bankCard.balance -= amount
        } else {
            throw Exception("Insufficient funds")
        }
        bankCardRepository.save(bankCard)

        // Record transaction with the updated balance
        transactionService.createTransaction(
            bankCard = bankCard,
            amount = amount,
            type = "WITHDRAW",
            description = null, // or provide a description if needed
            balanceAfterTransaction = bankCard.balance
        )

        //send notification
        notificationService.createNotification("Withdraw of $$amount successful", bankCard.appUser)

        return bankCard

    }

    @Transactional
    fun transferFunds(transferRequest: TransferRequest): BankCard {
        val sourceCard = bankCardRepository.findByCardNumber(transferRequest.sourceCardNumber)
            ?: throw IllegalArgumentException("Source card not found")

        val destinationCard = bankCardRepository.findByCardNumber(transferRequest.destinationCardNumber)
            ?: throw IllegalArgumentException("Destination card not found")

        if (sourceCard.balance < transferRequest.amount) {
            throw IllegalArgumentException("Insufficient funds")
        }

        println("Before transfer - Source balance: ${sourceCard.balance}, Destination balance: ${destinationCard.balance}")

        // Update balances
        sourceCard.balance -= transferRequest.amount
        destinationCard.balance += transferRequest.amount

        // Save updated balances
        bankCardRepository.save(sourceCard)
        bankCardRepository.save(destinationCard)

        println("After transfer - Source balance: ${sourceCard.balance}, Destination balance: ${destinationCard.balance}")

        // Record the transaction for the source card (TRANSFER)
        transactionService.createTransaction(
            bankCard = sourceCard,
            amount = transferRequest.amount,
            type = "TRANSFER",
            description = transferRequest.description,
            balanceAfterTransaction = sourceCard.balance // Pass the updated balance
        )

        // Record the transaction for the destination card (DEPOSIT)
        transactionService.createTransaction(
            bankCard = destinationCard,
            amount = transferRequest.amount,
            type = "DEPOSIT",
            description = transferRequest.description,
            balanceAfterTransaction = destinationCard.balance // Pass the updated balance
        )

        return sourceCard
    }




    private fun generateCardNumber(): String {
        val prefix = listOf("51", "52", "53", "54", "55").random() // MasterCard prefixes
        val cardNumberWithoutCheckDigit = (prefix + List(14) { Random.nextInt(0, 10) }.joinToString(""))
        val checkDigit = calculateCheckDigit(cardNumberWithoutCheckDigit)
        return cardNumberWithoutCheckDigit + checkDigit
    }

    private fun calculateCheckDigit(cardNumber: String): Int {
        val digits = cardNumber.map { it.toString().toInt() }
        val sum = digits.reversed().mapIndexed { index, digit ->
            if (index % 2 == 0) {
                val doubled = digit * 2
                if (doubled > 9) doubled - 9 else doubled
            } else {
                digit
            }
        }.sum()
        return (10 - (sum % 10)) % 10
    }

    private fun generateExpiryDate(): String {
        val expiry = LocalDate.now().plusYears(3)
        return expiry.format(DateTimeFormatter.ofPattern("MM/yy"))
    }

    private fun generateCvv(): String {
        return List(3) { Random.nextInt(0, 10) }.joinToString("")
    }

    fun findBankCardByNumber(cardNumber: String): BankCard? {
        return bankCardRepository.findByCardNumber(cardNumber)
    }

    fun findBankCardById(cardId: Long): BankCard? {
        return bankCardRepository.findById(cardId).orElseThrow()
    }

    fun getAllBankCard() : MutableList<BankCard> {
        return bankCardRepository.findAll()
    }
}