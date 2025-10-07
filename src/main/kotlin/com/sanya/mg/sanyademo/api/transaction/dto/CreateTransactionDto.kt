package com.sanya.mg.sanyademo.api.transaction.dto

import com.sanya.mg.sanyademo.common.TransactionType
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class CreateTransactionDto(
    @field:Schema(description = "Transaction type", example = "BUY")
    val type: TransactionType,
    @field:Schema(description = "Trading symbol", example = "BTCUSDT")
    val symbol: String,
    @field:Schema(description = "Quantity", example = "0.5")
    val quantity: BigDecimal,
    @field:Schema(description = "User ID", example = "1")
    val userId: Long,
)
