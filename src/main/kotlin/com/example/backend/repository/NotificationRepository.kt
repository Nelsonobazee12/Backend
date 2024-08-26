package com.example.backend.repository

import com.example.backend.bankAccount.Notification
import com.example.backend.Entities.users.AppUser
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification,Long> {
    fun findByAppUser(appUser: AppUser): List<Notification>
}
