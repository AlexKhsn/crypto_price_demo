package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.transaction.dto.TransactionDto
import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.TransactionRepository
import com.sanya.mg.sanyademo.repository.entity.Transaction
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
) {
    fun createTransaction(
        type: TransactionType,
        symbol: String,
        quantity: BigDecimal,
    ): TransactionDto {
        val forSave = Transaction(
            type = type,
            symbol = symbol,
            quantity = quantity,
        )

        val saved = transactionRepository.save(forSave)
        return TransactionDto fromEntity saved
    }

    fun getAllTransactions(): List<TransactionDto> {
        val transactions = transactionRepository.findAll()
        return transactions.map { transaction ->
            TransactionDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
                date = transaction.date,
            )
        }
    }

    fun getById(id: Long): TransactionDto {
        val transaction = transactionRepository.findById(id).get()
        return TransactionDto fromEntity transaction
    }

    fun getAllByType(type: TransactionType): List<TransactionDto> {
        val filtered = transactionRepository.findAllTransactionsByType(type)
        return filtered.map { transaction -> TransactionDto fromEntity transaction }
    }

    fun getAllBySymbol(symbol: String): List<TransactionDto> {
        val filtered = transactionRepository.findAllTransactionsBySymbol(symbol)
        return filtered.map { transaction -> TransactionDto fromEntity transaction }
    }

    fun deleteById(id: Long): TransactionDto {
        val deleted = transactionRepository.findById(id).get()
        transactionRepository.deleteById(id)
        return TransactionDto fromEntity deleted
    }

    fun updateById(
        id: Long,
        type: TransactionType,
        symbol: String,
        quantity: BigDecimal,
    ): TransactionDto {
        val transaction = transactionRepository.findById(id).get()
        val updated = Transaction(
            id = transaction.id!!,
            type = type,
            symbol = symbol,
            quantity = quantity,
        )

        transactionRepository.save(updated)

        return TransactionDto fromEntity updated
    }
}
