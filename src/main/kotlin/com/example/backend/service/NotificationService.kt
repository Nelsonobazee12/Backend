package com.example.backend.service

import com.example.backend.bankAccount.Notification
import com.example.backend.repository.NotificationRepository
import com.example.backend.Entities.users.AppUser
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class NotificationService(private val notificationRepository: NotificationRepository) {

    fun createNotification(message: String, appUser: AppUser): Notification {
        val notification = Notification(
            message = message,
            timestamp = LocalDateTime.now().toString(),
            appUser = appUser
        )
        return notificationRepository.save(notification)
    }

    fun getNotifications(appUser: AppUser): List<Notification> {
        return notificationRepository.findByAppUser(appUser)
    }
}
