package com.example.backend.repository

import com.example.backend.Entities.users.AppUser
import com.example.backend.bankAccount.BankCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface BankCardRepository : JpaRepository<BankCard, Long> {
    fun findByCardNumber(cardNumber: String): BankCard?

    @Query("SELECT bc FROM BankCard bc WHERE bc.appUser = :appUser")
    fun findByAppUser(@Param("appUser") appUser: AppUser): BankCard?

    fun findByAppUserId(userId: Long): BankCard?

}