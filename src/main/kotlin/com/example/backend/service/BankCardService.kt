package com.example.backend.service
import com.example.backend.bankAccount.BankCard
import com.example.backend.bankAccount.transaction.TransferRequest
import com.example.backend.exceptions.InsufficientFundsException
import com.example.backend.exceptions.NotFoundException
import com.example.backend.repository.AppUserRepository
import com.example.backend.repository.BankCardRepository
import jakarta.transaction.Transactional
import mu.KotlinLogging
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random


@Service
class BankCardService(
    private val bankCardRepository: BankCardRepository,
    private val appUserRepository: AppUserRepository,
    private val transactionService: TransactionService,
    private val notificationService: NotificationService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    private val log = KotlinLogging.logger {}

    @Transactional
    fun createBankCard(userId: Long, initialBalance: Double = 0.0): BankCard {
        val appUser = appUserRepository.findById(userId)
            .orElseThrow { NotFoundException("User not found with ID: $userId") }

        // Check if bank card exists in the repository
        val existingBankCard = bankCardRepository.findByAppUser(appUser)
        if (existingBankCard != null) {
            throw IllegalStateException("User already has a bank card")
        }

        val bankCard = BankCard(
            cardNumber = generateCardNumber(),
            expiryDate = generateExpiryDate(),
            cvv = generateCvv(),
            appUser = appUser,
            balance = initialBalance
        )

        // Save the bank card and update the user
        appUser.bankCard = bankCard
        bankCardRepository.save(bankCard)

        // Notify the user about the successful creation
        notificationService.createNotification("Bank Card Created successfully", appUser)

        // Notify and send email
        applicationEventPublisher.publishEvent(BankCardCreatedEvent(appUser))

        return bankCard
    }


    @Transactional
//    @CachePut(value = ["bankCards"], key = "#userId")
    fun addFunds(userId: Long, amount: Double): BankCard {

        val bankCard = bankCardRepository.findByAppUserId(userId)
            ?: throw NotFoundException("Bank card not found for user ID: $userId")
        bankCard.balance += amount
        val updatedBankCard = bankCardRepository.save(bankCard)

        transactionService.createTransaction(
            bankCard = updatedBankCard,
            amount = amount,
            type = "DEPOSIT",
            description = null,
            balanceAfterTransaction = updatedBankCard.balance
        )

        updatedBankCard.appUser?.let { notificationService.createNotification("Deposit of $$amount successful", it) }

        return updatedBankCard
    }

    @Transactional
//    @CachePut(value = ["bankCards"], key = "#userId")
    fun deductFunds(userId: Long, amount: Double): BankCard {
        val bankCard = bankCardRepository.findByAppUserId(userId)
            ?: throw NotFoundException("Bank card not found for user ID: $userId")
        if (bankCard.balance < amount) {
            throw InsufficientFundsException("Insufficient funds")
        }
        bankCard.balance -= amount
        val updatedBankCard = bankCardRepository.save(bankCard)

        transactionService.createTransaction(
            bankCard = updatedBankCard,
            amount = amount,
            type = "WITHDRAWAL",
            description = null,
            balanceAfterTransaction = updatedBankCard.balance
        )

        updatedBankCard.appUser?.let { notificationService.createNotification("Withdraw of $$amount successful", it) }

        return updatedBankCard
    }

    @Transactional
//    @CacheEvict(value = ["bankCards"], allEntries = true)
    fun transferFunds(transferRequest: TransferRequest, authenticatedUserId: Long): BankCard {
        val sourceCard = bankCardRepository.findByAppUserId(authenticatedUserId)
            ?: throw NotFoundException("Source card not found for the authenticated user")

        val destinationCard = bankCardRepository.findByCardNumber(transferRequest.destinationCardNumber)
            ?: throw NotFoundException("Destination card not found")

        if (sourceCard.balance < transferRequest.amount) {
            throw InsufficientFundsException("Insufficient funds")
        }

        sourceCard.balance -= transferRequest.amount
        destinationCard.balance += transferRequest.amount

        val updatedSourceCard = bankCardRepository.save(sourceCard)
        bankCardRepository.save(destinationCard)

        transactionService.createTransaction(
            bankCard = updatedSourceCard,
            amount = transferRequest.amount,
            type = "TRANSFER",
            description = transferRequest.description,
            balanceAfterTransaction = updatedSourceCard.balance
        )

        transactionService.createTransaction(
            bankCard = destinationCard,
            amount = transferRequest.amount,
            type = "DEPOSIT",
            description = transferRequest.description,
            balanceAfterTransaction = destinationCard.balance
        )

        updatedSourceCard.appUser?.let { notificationService.createNotification("Transfer was successful", it) }

        return updatedSourceCard
    }

//    @Cacheable(value = ["bankCards"], key = "#userId")
    fun getCardsByUserId(userId: Long): BankCard? {
        return bankCardRepository.findByAppUserId(userId)
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
}
