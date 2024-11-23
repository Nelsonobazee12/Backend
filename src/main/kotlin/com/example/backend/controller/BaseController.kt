package com.example.backend.controller

import com.example.backend.Entities.users.AppUser
import com.example.backend.security.AuthUtils
import com.example.backend.service.AppUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.server.ResponseStatusException

abstract class BaseController{
    @Autowired
    protected lateinit var authUtils: AuthUtils

    @Autowired
    protected lateinit var userService: AppUserService

    // Get current user from database
    protected fun getCurrentUser(authentication: Authentication): AppUser {
        val userId = authUtils.getCurrentUserId(authentication)
        return userService.findByAuth0Id(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }
}
