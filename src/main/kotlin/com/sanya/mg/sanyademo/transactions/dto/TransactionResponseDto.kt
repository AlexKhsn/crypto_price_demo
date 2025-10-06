package com.sanya.mg.sanyademo.transactions.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class TransactionResponseDto(
    val id: Long,
    val type: String,
    val symbol: String,
    val quantity: BigDecimal,
    val price: BigDecimal,
    val date: LocalDate,
    val totalValue: BigDecimal = quantity * price,
    val daysAgo: Long = ChronoUnit.DAYS.between(date, LocalDate.now()),
)
