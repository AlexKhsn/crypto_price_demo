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
        return TransactionDto(
            id = saved.id!!,
            saved.type,
            saved.symbol,
            saved.quantity,
            saved.date,
        )
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
        return TransactionDto(
            transaction.id!!,
            transaction.type,
            transaction.symbol,
            transaction.quantity,
            transaction.date,
        )
    }

    fun getAllByType(type: TransactionType): List<TransactionDto> {
        val filtered = transactionRepository.findAllTransactionsByType(type)
        return filtered.map { transaction ->
            TransactionDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
                date = transaction.date,
            )
        }
    }

    fun getAllBySymbol(symbol: String): List<TransactionDto> {
        val filtered = transactionRepository.findAllTransactionsBySymbol(symbol)
        return filtered.map { transaction ->
            TransactionDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
                date = transaction.date,
            )
        }
    }

    fun deleteById(id: Long): TransactionDto {
        val deleted = transactionRepository.findById(id).get()
        transactionRepository.deleteById(id)
        return TransactionDto(
            deleted.id!!,
            deleted.type,
            deleted.symbol,
            deleted.quantity,
            deleted.date,
        )
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

        return TransactionDto(
            updated.id!!,
            updated.type,
            updated.symbol,
            updated.quantity,
            updated.date,
        )
    }
}
