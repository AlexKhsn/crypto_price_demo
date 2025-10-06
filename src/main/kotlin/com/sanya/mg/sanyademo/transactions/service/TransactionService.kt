package com.sanya.mg.sanyademo.transactions.service

import com.sanya.mg.sanyademo.transactions.dto.CreateTransactionDto
import com.sanya.mg.sanyademo.transactions.dto.TransactionResponseDto
import com.sanya.mg.sanyademo.transactions.entity.Transaction
import com.sanya.mg.sanyademo.transactions.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
) {
    fun createTransaction(request: CreateTransactionDto): TransactionResponseDto {
        val forSave = Transaction(
            type = request.type,
            symbol = request.symbol,
            quantity = request.quantity,
            price = request.price,
        )

        val saved = transactionRepository.save(forSave)
        return TransactionResponseDto(
            id = saved.id!!,
            saved.type,
            saved.symbol,
            saved.quantity,
            saved.price,
            saved.date,
        )
    }

    fun getAllTransactions(): List<TransactionResponseDto> {
        val transactions = transactionRepository.findAll()
        return transactions.map { transaction ->
            TransactionResponseDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
                price = transaction.price,
                date = transaction.date,
            )
        }
    }

    fun getById(id: Long): TransactionResponseDto {
        val transaction = transactionRepository.findById(id).get()
        return TransactionResponseDto(
            transaction.id!!,
            transaction.type,
            transaction.symbol,
            transaction.quantity,
            transaction.price,
            transaction.date,
        )
    }

    fun getAllByType(type: String): List<TransactionResponseDto> {
        val filtered = transactionRepository.findAllTransactionsByType(type)
        return filtered.map { transaction ->
            TransactionResponseDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
                price = transaction.price,
                date = transaction.date,
            )
        }
    }

    fun getAllBySymbol(symbol: String): List<TransactionResponseDto> {
        val filtered = transactionRepository.findAllTransactionsBySymbol(symbol)
        return filtered.map { transaction ->
            TransactionResponseDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
                price = transaction.price,
                date = transaction.date,
            )
        }
    }

    fun deleteById(id: Long): TransactionResponseDto {
        val deleted = transactionRepository.findById(id).get()
        transactionRepository.deleteById(id)
        return TransactionResponseDto(
            deleted.id!!,
            deleted.type,
            deleted.symbol,
            deleted.quantity,
            deleted.price,
            deleted.date,
        )
    }

    fun updateById(id: Long, request: CreateTransactionDto): TransactionResponseDto {
        val transaction = transactionRepository.findById(id).get()
        val updated = Transaction(
            transaction.id!!,
            request.type,
            symbol = request.symbol,
            quantity = request.quantity,
            price = request.price,
        )

        transactionRepository.save(updated)

        return TransactionResponseDto(
            updated.id!!,
            updated.type,
            updated.symbol,
            updated.quantity,
            updated.price,
            updated.date,
        )
    }

    fun getStatisticsBySymbol(symbol: String): String {
        val filtered = transactionRepository.findAllTransactionsBySymbol(symbol)
        val buyTransactions = filtered.filter { transaction -> transaction.type == "BUY" }
        val sellTransactions = filtered.filter { transaction -> transaction.type == "SELL" }
        val totalBuyAmount = buyTransactions.sumOf { it.quantity }
        val totalSellAmount = sellTransactions.sumOf { it.quantity }
        val buyAverage = buyTransactions.sumOf { it.price } / buyTransactions.size.toBigDecimal()
        val sellAverage = sellTransactions.sumOf { it.price } / sellTransactions.size.toBigDecimal()

        return """
             - Сколько всего куплено: $totalBuyAmount
             - Сколько всего продано: $totalSellAmount
             - Средняя цена покупки: $buyAverage
             - Средняя цена продажи: $sellAverage
            """.trimIndent()
    }
}
