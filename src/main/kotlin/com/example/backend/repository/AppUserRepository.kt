package com.example.backend.repository

import com.example.backend.Entities.users.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE u.email = :email")
    fun findByEmail(email: String): AppUser?

    fun existsByEmail(newEmail: String): Boolean
}