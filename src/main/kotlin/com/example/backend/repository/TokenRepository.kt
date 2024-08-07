package com.example.backend.repository

import com.example.backend.token.Token
import org.springframework.stereotype.Repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

@Repository
interface TokenRepository : JpaRepository<Token, Long> {

    @Query(
        """
        select t from Token t inner join AppUser u
        on t.appUser.id = u.id
        where u.id = :id and (t.isExpired = false or t.isRevoked = false)
        """
    )
    fun findAllValidTokenByUser(id: Long): List<Token>?
    fun findByToken(token: String): Token?

//    @Modifying
//    @Query("DELETE FROM Token t WHERE t.appUser.id = :userId")
//    fun deleteByUserId(@Param("userId") userId: Long)
}

