//package com.example.backend.controller
//
//import com.example.backend.bankAccount.transaction.TransactionResponse
//import com.example.backend.service.BankCardService
//import com.example.backend.service.TransactionService
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//
//
//@RestController
//@RequestMapping("/transactions")
//class TransactionController(private val transactionService: TransactionService
//private val bankCardService: BankCardService) {
//
//    @GetMapping("/history/{cardId}")
//    fun getTransactionHistory(@PathVariable cardId: Long): List<TransactionResponse> {
//        val bankCard = bankCardService.findBankCardById(cardId)
//        return transactionService.getTransactionHistory(bankCard).map { transaction ->
//            TransactionResponse(
//                transactionId = transaction.transactionId,
//                amount = transaction.amount,
//                type = transaction.type,
//                description = transaction.description,
//                timestamp = transaction.timestamp,
//                cardNumber = transaction.bankCard.cardNumber!!,
//                downloadLink = "/transactions/download/${transaction.transactionId}"
//            )
//        }
//    }
//
//    @PostMapping("/create")
//    fun createTransaction(@RequestBody request: TransactionRequest): ResponseEntity<TransactionResponse> {
//        val bankCard = bankCardService.findBankCardById(request.cardId)
//        val transaction = transactionService.createTransaction(
//            bankCard = bankCard,
//            amount = request.amount,
//            type = request.type,
//            description = request.description
//        )
//        return ResponseEntity.ok(
//            TransactionResponse(
//                transactionId = transaction.transactionId,
//                amount = transaction.amount,
//                type = transaction.type,
//                description = transaction.description,
//                timestamp = transaction.timestamp,
//                cardNumber = transaction.bankCard.cardNumber!!,
//                downloadLink = "/transactions/download/${transaction.transactionId}"
//            )
//        )
//    }
//}
