//package com.example.backend.controller
//
//import com.example.backend.bankAccount.transaction.SearchEntity
//import com.example.backend.service.SearchService
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/api/search")
//class SearchController(
//    private val searchService: SearchService
//) {
//
//    @PostMapping("/activities")
//    fun searchActivities(@RequestBody searchEntity: SearchEntity): ResponseEntity<Map<String, List<Any>>> {
//        val activities = searchService.searchActivities(searchEntity.searchString)
//
//        return if (activities != null && (activities["transactions"]!!.isNotEmpty() || activities["notifications"]!!.isNotEmpty())) {
//            ResponseEntity.ok(activities)
//        } else {
//            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
//        }
//    }
//}
//
//
//
