package com.example.backend.controller

import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.Notification
import com.example.backend.bankAccount.transaction.Transaction
import com.example.backend.bankAccount.transaction.TransferRequest
import com.example.backend.repository.BankCardRepository
import com.example.backend.repository.TransactionRepository
import com.example.backend.service.BankCardService
import com.example.backend.service.NotificationService
import com.example.backend.service.TransactionService
import com.example.backend.Entities.users.AppUser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/")
class BankCardController (
    private val bankCardService: BankCardService,
    private val transactionService: TransactionService,
    private val notificationService: NotificationService,
    private val transactionRepository: TransactionRepository,
    private val bankCardRepository: BankCardRepository
){


    @GetMapping("/card-id/{cardId}")
    fun getBankCard(@PathVariable cardId: Long): ResponseEntity<BankCard> {
        val bankCard = bankCardService.findBankCardById(cardId)
        return if (bankCard != null) {
            ResponseEntity.ok(bankCard)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/cards/all")
    fun getAllBankCards(): ResponseEntity<List<BankCard>> {
        return ResponseEntity.ok(bankCardService.getAllBankCard())
        }

    @PostMapping("/{userId}/create-new-card")
    fun createBankCard(@PathVariable userId: Long): BankCard {
        return bankCardService.createBankCard(userId)
    }

    @PostMapping("/{cardId}/add-funds")
    fun addFunds(@PathVariable cardId: Long, @RequestParam amount: Double): ResponseEntity<BankCard> {
        val updatedCard = bankCardService.addFunds(cardId, amount)
        return ResponseEntity.ok(updatedCard)
    }

    @PostMapping("/{cardId}/deduct-funds")
    fun deductFunds(@PathVariable cardId: Long, @RequestParam amount: Double): ResponseEntity<BankCard> {
        val updatedCard = bankCardService.deductFunds(cardId, amount)
        return ResponseEntity.ok(updatedCard)
    }

    @PostMapping("/transfer")
    fun transferFunds(@RequestBody transferRequest: TransferRequest): ResponseEntity<BankCard> {
        return try {
            val updatedSourceCard = bankCardService.transferFunds(transferRequest)
            ResponseEntity.ok(updatedSourceCard)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        }
    }

    @GetMapping("bank-card/transactions")
    fun getTransactions(@RequestParam cardNumber: String): List<Transaction> {
        val bankCard = bankCardRepository.findByCardNumber(cardNumber)
            ?: throw IllegalArgumentException("Card not found")
        return transactionService.getTransactionHistory(bankCard)
    }

    @GetMapping("bank-card/notification")
    fun getNotifications(): List<Notification> {
        val currentUser = SecurityContextHolder.getContext().authentication.principal as AppUser
        return notificationService.getNotifications(currentUser)
    }
}