package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.transaction.dto.CreateTransactionDto
import com.sanya.mg.sanyademo.api.transaction.dto.TransactionResponseDto
import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.TransactionRepository
import com.sanya.mg.sanyademo.repository.entity.Transaction
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
        )

        val saved = transactionRepository.save(forSave)
        return TransactionResponseDto(
            id = saved.id!!,
            saved.type,
            saved.symbol,
            saved.quantity,
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
            transaction.date,
        )
    }

    fun getAllByType(type: TransactionType): List<TransactionResponseDto> {
        val filtered = transactionRepository.findAllTransactionsByType(type)
        return filtered.map { transaction ->
            TransactionResponseDto(
                id = transaction.id!!,
                type = transaction.type,
                symbol = transaction.symbol,
                quantity = transaction.quantity,
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
        )

        transactionRepository.save(updated)

        return TransactionResponseDto(
            updated.id!!,
            updated.type,
            updated.symbol,
            updated.quantity,
            updated.date,
        )
    }
}
