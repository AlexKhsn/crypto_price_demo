package com.sanya.mg.sanyademo.api.transaction.dto

import com.sanya.mg.sanyademo.common.TransactionType
import java.math.BigDecimal

data class CreateTransactionDto(
    val type: TransactionType,
    val symbol: String,
    val quantity: BigDecimal,
)
