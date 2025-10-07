package com.sanya.mg.sanyademo.api.transaction.dto

import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.entity.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class TransactionDto(
    val id: Long,
    val type: TransactionType,
    val symbol: String,
    val quantity: BigDecimal,
    val date: LocalDate,
    val daysAgo: Long = ChronoUnit.DAYS.between(date, LocalDate.now()),
    val userId: Long,
) {
    companion object {
        infix fun fromEntity(entity: Transaction): TransactionDto {
            return TransactionDto(
                entity.id!!,
                entity.type,
                entity.symbol,
                entity.quantity,
                entity.date,
                userId = entity.user.id!!,
            )
        }
    }
}
