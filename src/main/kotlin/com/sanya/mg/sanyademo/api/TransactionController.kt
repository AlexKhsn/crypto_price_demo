package com.sanya.mg.sanyademo.api

import com.sanya.mg.sanyademo.dto.CreateTransactionDto
import com.sanya.mg.sanyademo.dto.TransactionResponseDto
import com.sanya.mg.sanyademo.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    val transactionService: TransactionService,
) {
    @PostMapping
    fun addTransaction(
        @RequestBody
        request: CreateTransactionDto,
    ): TransactionResponseDto {
        val newTransaction = transactionService.createTransaction(request)
        return newTransaction
    }

    @GetMapping
    fun getTransactions(): ResponseEntity<List<TransactionResponseDto>> {
        val result = transactionService.getAllTransactions()
        return ResponseEntity.ok().body(result)
    }

    @GetMapping("/{id}")
    fun getTransactionById(
        @PathVariable id: Long,
    ): ResponseEntity<TransactionResponseDto> {
        try {
            val transaction = transactionService.getById(id)
            return ResponseEntity.ok().body(transaction)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/type/{type}")
    fun getTransactionsByType(
        @PathVariable type: String,
    ): ResponseEntity<List<TransactionResponseDto>> {
        val filteredTransactions = transactionService.getAllByType(type)
        return ResponseEntity.ok().body(filteredTransactions)
    }

    // Спросить по поводу enum класса для хранения типов операций и символа

    @GetMapping("/symbol/{symbol}")
    fun getTransactionsBySymbol(
        @PathVariable symbol: String,
    ): ResponseEntity<List<TransactionResponseDto>> {
        val filtered = transactionService.getAllBySymbol(symbol)
        return ResponseEntity.ok().body(filtered)
    }

    @DeleteMapping("/{id}")
    fun deleteTransaction(
        @PathVariable id: Long,
    ): ResponseEntity<TransactionResponseDto> {
        try {
            val deleted = transactionService.deleteById(id)
            return ResponseEntity.ok().body(deleted)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun updateTransaction(
        @PathVariable id: Long,
        @RequestBody request: CreateTransactionDto,
    ): ResponseEntity<TransactionResponseDto> {
        try {
            val transaction = transactionService.updateById(id, request)
            return ResponseEntity.ok().body(transaction)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/stats/{symbol}")
    fun getStatisticsBySymbol(
        @PathVariable symbol: String,
    ): String {
        val statistics = transactionService.getStatisticsBySymbol(symbol)
        return statistics
    }
}
