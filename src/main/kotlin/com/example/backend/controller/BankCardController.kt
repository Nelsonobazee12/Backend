package com.example.backend.controller

import com.example.backend.Entities.users.AppUser
import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.Notification
import com.example.backend.bankAccount.transaction.Transaction
import com.example.backend.bankAccount.transaction.TransferRequest
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.BankCardRepository
import com.example.backend.repository.TransactionRepository
import com.example.backend.service.BankCardService
import com.example.backend.service.NotificationService
import com.example.backend.service.TransactionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/cards")
class BankCardController(
    private val bankCardService: BankCardService,
    private val transactionService: TransactionService,
    private val notificationService: NotificationService,
    private val transactionRepository: TransactionRepository,
    private val bankCardRepository: BankCardRepository,
    private val appUserRepository: AppUserRepository
){


    @GetMapping("/user_card")
    fun getUserCards(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<BankCard>> {
        val appUser = userDetails as? AppUser ?: throw IllegalArgumentException("Invalid user details")
        val userCards = appUser.id?.let { bankCardService.getCardsByUserId(it) }
        return ResponseEntity.ok(userCards)
    }

    @GetMapping("/all")
    fun getAllBankCards(): ResponseEntity<List<BankCard>> {
        return ResponseEntity.ok(bankCardService.getAllBankCard())
        }

    @PostMapping("/create-new-card")
    fun createBankCard(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<BankCard> {
        val appUser = userDetails as? AppUser ?: throw IllegalArgumentException("Invalid user details")
        val newCard = appUser.id?.let { bankCardService.createBankCard(it) }
        return ResponseEntity.ok(newCard)
    }

    @PostMapping("/add-funds")
    fun addFunds(@AuthenticationPrincipal userDetails: UserDetails, @RequestParam amount: Double): ResponseEntity<BankCard> {
        val appUser = userDetails as? AppUser ?: throw IllegalArgumentException("Invalid user details")
        val addFunds = appUser.id?.let { bankCardService.addFunds(it,amount) }
        return ResponseEntity.ok(addFunds)
    }

    @PostMapping("/deduct-funds")
    fun deductFunds(@AuthenticationPrincipal userDetails: UserDetails, @RequestParam amount: Double): ResponseEntity<BankCard> {
        val appUser = userDetails as? AppUser ?: throw IllegalArgumentException("Invalid user details")
        val deductFunds = appUser.id?.let { bankCardService.deductFunds(it,amount) }
        return ResponseEntity.ok(deductFunds)
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
    fun getTransactions(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<Transaction>> {

        val appUser = appUserRepository.findByEmail(userDetails.username)
            ?: throw IllegalArgumentException("User not found")

        val bankCard = bankCardRepository.findByAppUser(appUser)
            ?: throw IllegalArgumentException("Bank card not found for the user")

        return ResponseEntity.ok().body(transactionService.getTransactionHistory(bankCard))
    }


    @GetMapping("bank-card/notification")
    fun getNotifications(): List<Notification> {
        val currentUser = SecurityContextHolder.getContext().authentication.principal as AppUser
        return notificationService.getNotifications(currentUser)
    }
}