//package com.example.backend.service
//
//import com.example.backend.repository.NotificationRepository
//import com.example.backend.repository.TransactionRepository
//import jakarta.transaction.Transactional
//import org.springframework.stereotype.Service
//
//@Service
//@Transactional
//class SearchService(
//    private val transactionRepository: TransactionRepository,
//    private val notificationRepository: NotificationRepository,
//    private val redisTemplate: RedisTemplate<String, Map<String, List<Any>>>
////    private val redisTemplate: RedisTemplate<String, Any>
//) {
//
//    companion object {
//        private const val SEARCH_CACHE_KEY = "userSearchCache:"
//    }
//
//    fun searchActivities(searchString: String): Map<String, List<Any>>? {
//        val cacheKey = SEARCH_CACHE_KEY + searchString
//
//        // Check if result is in Redis cache
//        val cachedResult = redisTemplate.opsForValue().get(cacheKey)
//        if (cachedResult != null) {
//            return cachedResult as Map<String, List<Any>>?
//        }
//
//        // Search for notifications that contain the search string
//        val notifications = notificationRepository.findAllByMessageContainingIgnoreCase(searchString)
//
//        // Search for transactions based on keywords (adjust this according to your transaction entity)
//        val transactions = transactionRepository.findAllByDescriptionContainingIgnoreCase(searchString)
//
//        // Prepare the result map to include transactions and notifications
//        val result = mapOf(
//            "transactions" to transactions,
//            "notifications" to notifications
//        )
//
//        // Store the result in Redis for future queries
//        redisTemplate.opsForValue().set(cacheKey, result)
//
//        return result
//    }
//}
//
//
