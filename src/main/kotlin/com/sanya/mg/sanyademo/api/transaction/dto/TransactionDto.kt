package com.sanya.mg.sanyademo.api.transaction.dto

import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.entity.TransactionEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class TransactionDto(
    @field:Schema(description = "Transaction ID", example = "1")
    val id: Long,
    @field:Schema(description = "Transaction type", example = "BUY")
    val type: TransactionType,
    @field:Schema(description = "Trading symbol", example = "BTCUSDT")
    val symbol: String,
    @field:Schema(description = "Quantity", example = "0.5")
    val quantity: BigDecimal,
    @field:Schema(description = "Transaction date", example = "2025-10-01")
    val date: LocalDate,
    @field:Schema(description = "Days since transaction", example = "6")
    val daysAgo: Long = ChronoUnit.DAYS.between(date, LocalDate.now()),
    @field:Schema(description = "User ID", example = "1")
    val userId: Long,
) {
    companion object {
        infix fun fromEntity(entity: TransactionEntity): TransactionDto {
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
