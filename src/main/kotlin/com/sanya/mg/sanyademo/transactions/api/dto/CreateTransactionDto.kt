package com.sanya.mg.sanyademo.transactions.api.dto

import com.sanya.mg.sanyademo.transactions.common.TransactionType
import java.math.BigDecimal

data class CreateTransactionDto(
    val type: TransactionType,
    val symbol: String,
    val quantity: BigDecimal,
    val price: BigDecimal,
)
