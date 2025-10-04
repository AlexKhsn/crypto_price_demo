package com.sanya.mg.sanyademo.dto

import java.math.BigDecimal

data class CreateTransactionDto(
    val type: String,
    val symbol: String,
    val quantity: BigDecimal,
    val price: BigDecimal,
)
