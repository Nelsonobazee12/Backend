package com.example.backend.controller

import com.example.backend.Entities.users.AppUser
import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.Notification
import com.example.backend.bankAccount.transaction.DepositRequest
import com.example.backend.bankAccount.transaction.Transaction
import com.example.backend.bankAccount.transaction.TransferRequest
import com.example.backend.exceptions.NotFoundException
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.BankCardRepository
import com.example.backend.repository.TransactionRepository
import com.example.backend.service.BankCardService
import com.example.backend.service.NotificationAndTransaction
import com.example.backend.service.NotificationService
import com.example.backend.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
) : BaseController(){


    @GetMapping("/user_card")
    fun getUserCards(authentication: Authentication): ResponseEntity<BankCard> {
        val appUser = getCurrentUser(authentication)
        val userCards = appUser.id?.let { bankCardService.getCardsByUserId(it) }
        return ResponseEntity.ok(userCards)
    }

//    @GetMapping("/all")
//    fun getAllBankCards(): ResponseEntity<List<BankCard>> {
//        return ResponseEntity.ok(bankCardService.getAllBankCard())
//        }

    @PostMapping("/create-new-card")
    fun createBankCard(authentication: Authentication): ResponseEntity<BankCard> {
        val appUser = getCurrentUser(authentication)
        val newCard = appUser.id?.let { bankCardService.createBankCard(it) }
        return ResponseEntity.ok(newCard)
    }

    @PostMapping("/add-funds")
    fun addFunds(
        authentication: Authentication,
        @RequestBody @Valid depositRequest: DepositRequest
    ): ResponseEntity<BankCard> {
        val appUser = getCurrentUser(authentication)
        val userId = appUser.id ?: throw IllegalStateException("User ID is null")

        val updatedCard = depositRequest.amount?.let { bankCardService.addFunds(userId, it) }
        return ResponseEntity.ok(updatedCard)
    }

    @PostMapping("/deduct-funds")
    fun deductFunds(authentication: Authentication, @RequestBody @Valid depositRequest: DepositRequest): ResponseEntity<BankCard> {
        val appUser = getCurrentUser(authentication)
        val userId = appUser.id ?: throw IllegalStateException("User ID is null")

        val updatedCard = depositRequest.amount?.let { bankCardService.deductFunds(userId, it) }
        return ResponseEntity.ok(updatedCard)
    }

    @PostMapping("/transfer")
    fun transferFunds(@RequestBody @Valid transferRequest: TransferRequest, authentication: Authentication): ResponseEntity<BankCard> {
        return try {
            // Cast to your custom UserDetails class
            val myUserDetails = getCurrentUser(authentication)

            // Extract the authenticated user's ID
            val authenticatedUserId = myUserDetails.id

            // Call the service method with the transferRequest and the authenticatedUserId
            val updatedSourceCard = authenticatedUserId?.let { bankCardService.transferFunds(transferRequest, it) }
            ResponseEntity.ok(updatedSourceCard)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)
        } catch (e: NotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }



    @GetMapping("bank-card/transactions")
    fun getTransactions(authentication: Authentication): ResponseEntity<List<Transaction>> {
        val appUser = getCurrentUser(authentication)

        val bankCard = bankCardRepository.findByAppUser(appUser)
            ?: throw IllegalArgumentException("Bank card not found for the user")

        return ResponseEntity.ok().body(transactionService.getTransactionHistory(bankCard))
    }


    @GetMapping("bank-card/notification")
    fun getNotifications(authentication: Authentication): ResponseEntity<List<NotificationAndTransaction>> {
        val user = getCurrentUser(authentication)
        val notificationsAndTransactions = notificationService.getNotificationsAndTransactions(user)
        return ResponseEntity.ok(notificationsAndTransactions)
    }


}