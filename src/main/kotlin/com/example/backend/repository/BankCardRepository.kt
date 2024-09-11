package com.example.backend.repository

import com.example.backend.bankAccount.BankCard
import com.example.backend.Entities.users.AppUser
import org.springframework.data.jpa.repository.JpaRepository


interface BankCardRepository : JpaRepository<BankCard, Long> {
    fun findByCardNumber(cardNumber: String): BankCard?
    fun findByAppUser(appUser: AppUser?): BankCard?
    fun findByAppUserId(userId: Long): List<BankCard>
}