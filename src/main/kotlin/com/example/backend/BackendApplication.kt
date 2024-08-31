package com.example.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example.backend"])
class BackendApplication

    fun main(args: Array<String>) {
        runApplication<BackendApplication>(*args)
    }
