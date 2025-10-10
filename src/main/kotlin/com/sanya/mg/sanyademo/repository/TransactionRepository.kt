package com.sanya.mg.sanyademo.repository

import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findAllTransactionsByType(type: TransactionType): List<TransactionEntity>

    fun findAllTransactionsBySymbol(symbol: String): List<TransactionEntity>
}
