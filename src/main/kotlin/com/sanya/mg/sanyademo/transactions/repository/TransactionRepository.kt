package com.sanya.mg.sanyademo.transactions.repository

import com.sanya.mg.sanyademo.transactions.common.TransactionType
import com.sanya.mg.sanyademo.transactions.repository.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllTransactionsByType(type: TransactionType): List<Transaction>

    fun findAllTransactionsBySymbol(symbol: String): List<Transaction>
}
