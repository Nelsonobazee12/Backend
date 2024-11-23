package com.example.backend.service

import com.example.backend.bankAccount.Notification
import com.example.backend.repository.NotificationRepository
import com.example.backend.Entities.users.AppUser
import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.Transaction
import com.example.backend.exceptions.NotFoundException
import com.example.backend.repository.BankCardRepository
import com.example.backend.repository.TransactionRepository
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val transactionRepository: TransactionRepository,
    private val bankCardRepository: BankCardRepository,
) {

    private val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)

    @Transactional
//    @CacheEvict(value = ["notifications"], key = "#appUser.email")
    fun createNotification(message: String, appUser: AppUser): Notification {
        return try {
            val notification = Notification(
                message = message,
                timestamp = LocalDateTime.now().toString(),
                appUser = appUser
            )
            val savedNotification = notificationRepository.save(notification)
            logger.info("Notification created for user ${appUser.id}: $savedNotification")
            savedNotification
        } catch (e: Exception) {
            logger.error("Error creating notification for user ${appUser.id}", e)
            throw RuntimeException("Failed to create notification", e)
        }
    }

////    @Cacheable(value = ["notifications"], key = "#appUser.email")
//    fun getNotifications(appUser: AppUser): List<Notification, Transaction> {
//        return notificationRepository.findByAppUser(appUser)
//            .sortedByDescending { it.timestamp }
//    }

    fun getNotificationsAndTransactions(appUser: AppUser): List<NotificationAndTransaction> {
        // Fetch notifications
        val notifications = notificationRepository.findByAppUser(appUser)
            .sortedWith(compareByDescending { it.timestamp })

        // Fetch the single bank card
        val bankCard = bankCardRepository.findByAppUser(appUser)
            ?: throw NotFoundException("No bank card found for user with ID: ${appUser.id}")

        // Fetch transactions
        val transactions = transactionRepository.findByBankCard(bankCard)
            .sortedWith(compareByDescending { it.timestamp })

        // Combine notifications and transactions
        return (notifications.map { NotificationAndTransaction(it, null) } +
                transactions.map { NotificationAndTransaction(null, it) })
            .sortedWith(compareByDescending {
                it.notification?.timestamp ?: it.transaction?.timestamp
            })
    }



}

data class NotificationAndTransaction(
    val notification: Notification?,
    val transaction: Transaction?
)

