package com.example.backend.repository

import com.example.backend.users.AppUser
import org.springframework.data.jpa.repository.JpaRepository

interface AppUserRepository : JpaRepository<AppUser, Long> {
    fun findByEmail(email: String): AppUser?
    fun existsByEmail(newEmail: String): Boolean
}