package com.sanya.mg.sanyademo.api.transaction.dto

import com.sanya.mg.sanyademo.common.TransactionType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class TransactionResponseDto(
    val id: Long,
    val type: TransactionType,
    val symbol: String,
    val quantity: BigDecimal,
    val price: BigDecimal,
    val date: LocalDate,
    val totalValue: BigDecimal = quantity * price,
    val daysAgo: Long = ChronoUnit.DAYS.between(date, LocalDate.now()),
)
