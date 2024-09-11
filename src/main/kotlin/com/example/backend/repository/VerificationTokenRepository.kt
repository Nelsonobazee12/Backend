package com.example.backend.repository

import com.example.backend.token.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository : JpaRepository<VerificationToken, Long>{
    fun findByToken(token: String): VerificationToken?

//    fun deleteByAppUserId(userId : Long)

}
