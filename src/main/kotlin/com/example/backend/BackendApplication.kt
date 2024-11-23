package com.example.backend

import com.example.backend.Entities.users.AppUser
import com.example.backend.Entities.users.Role
import com.example.backend.repository.AppUserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching


@EnableCaching
@SpringBootApplication(
    scanBasePackages = ["com.example.backend"],
    exclude = [RedisRepositoriesAutoConfiguration::class]
)
class BackendApplication
//    (
//    private val appUserRepository: AppUserRepository,
//    private val passwordEncoder: PasswordEncoder
//) : CommandLineRunner {
//
//    override fun run(vararg args: String?) {
//        createAdminUserIfNotExist()
//    }
//
//    private fun createAdminUserIfNotExist() {
//        val adminEmail = "nelsonobazee12@gmail.com"
//        try {
//            // Check if an admin with the given email already exists
//            val adminUser = appUserRepository.findByEmail(adminEmail)
//            if (adminUser == null) {
//                val admin = AppUser(
//                    name = "Admin",
//                    email = adminEmail,
//                    password = passwordEncoder.encode("admin_password"), // Use a secure password
//                    roles = Role.ADMIN,
//                    enabled = true,
//                    isTwoFactorAuthEnabled = false
//                )
//
//                appUserRepository.save(admin)
//                println("Admin user created with email: $adminEmail")
//            } else {
//                println("Admin user already exists.")
//            }
//        } catch (e: Exception) {
//            println("Error creating admin user: ${e.message}")
//            e.printStackTrace()
//        }
//    }
//
//}

fun main(args: Array<String>) {
    runApplication<BackendApplication>(*args)
}



