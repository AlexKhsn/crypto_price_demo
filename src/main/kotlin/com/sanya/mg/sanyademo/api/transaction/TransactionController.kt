package com.sanya.mg.sanyademo.api.transaction

import com.sanya.mg.sanyademo.api.transaction.dto.CreateTransactionDto
import com.sanya.mg.sanyademo.api.transaction.dto.TransactionDto
import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.service.TransactionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
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
@Tag(name = "Transactions", description = "Transaction management API")
class TransactionController(
    val transactionService: TransactionService,
) {
    @Operation(
        summary = "Create transaction",
        description = "Creates a new transaction (deprecated - transactions are created automatically when assets change)",
    )
    @Deprecated("Transaction could be created only when assets are changing")
    @PostMapping
    fun addTransaction(
        @RequestBody request: CreateTransactionDto,
    ): ResponseEntity<TransactionDto> {
//        val newTransaction = transactionService.createTransaction(
//            type = request.type,
//            symbol = request.symbol,
//            quantity = request.quantity,
//        )
//        return newTransaction
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    @Operation(summary = "Get all transactions", description = "Returns a list of all transactions")
    @GetMapping
    fun getTransactions(): ResponseEntity<List<TransactionDto>> {
        val result = transactionService.getAllTransactions()
        return ResponseEntity.ok().body(result)
    }

    @Operation(summary = "Get transaction by ID", description = "Returns a single transaction by its ID")
    @GetMapping("/{id}")
    fun getTransactionById(
        @PathVariable id: Long,
    ): ResponseEntity<TransactionDto> {
        try {
            val transaction = transactionService.getById(id)
            return ResponseEntity.ok().body(transaction)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Get transactions by type", description = "Returns all transactions filtered by type (BUY or SELL)")
    @GetMapping("/type/{type}")
    fun getTransactionsByType(
        @Parameter(schema = Schema(implementation = TransactionType::class))
        @PathVariable type: TransactionType,
    ): ResponseEntity<List<TransactionDto>> {
        val filteredTransactions = transactionService.getAllByType(type)
        return ResponseEntity.ok().body(filteredTransactions)
    }

    @Operation(summary = "Get transactions by symbol", description = "Returns all transactions filtered by trading symbol")
    @GetMapping("/symbol/{symbol}")
    fun getTransactionsBySymbol(
        @PathVariable symbol: String,
    ): ResponseEntity<List<TransactionDto>> {
        val filtered = transactionService.getAllBySymbol(symbol)
        return ResponseEntity.ok().body(filtered)
    }

    @Operation(summary = "Delete transaction", description = "Deletes a transaction (deprecated - transactions cannot be deleted)")
    @Deprecated("Transaction cannot be deleted or changed")
    @DeleteMapping("/{id}")
    fun deleteTransaction(
        @PathVariable id: Long,
    ): ResponseEntity<TransactionDto> {
//        try {
//            val deleted = transactionService.deleteById(id)
//            return ResponseEntity.ok().body(deleted)
//        } catch (e: NoSuchElementException) {
//            return ResponseEntity.notFound().build()
//        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }

    @Operation(summary = "Update transaction", description = "Updates a transaction (deprecated - transactions cannot be modified)")
    @Deprecated("Transaction cannot be deleted or changed")
    @PutMapping("/{id}")
    fun updateTransaction(
        @PathVariable id: Long,
        @RequestBody request: CreateTransactionDto,
    ): ResponseEntity<TransactionDto> {
//        try {
//            val transaction = transactionService.updateById(
//                id = id,
//                type = request.type,
//                symbol = request.symbol,
//                quantity = request.quantity,
//            )
//            return ResponseEntity.ok().body(transaction)
//        } catch (e: NoSuchElementException) {
//            return ResponseEntity.notFound().build()
//        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build()
    }
}
