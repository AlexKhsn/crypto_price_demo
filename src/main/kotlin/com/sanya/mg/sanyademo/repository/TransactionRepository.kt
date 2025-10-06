package com.sanya.mg.sanyademo.repository

import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllTransactionsByType(type: TransactionType): List<Transaction>

    fun findAllTransactionsBySymbol(symbol: String): List<Transaction>
}
