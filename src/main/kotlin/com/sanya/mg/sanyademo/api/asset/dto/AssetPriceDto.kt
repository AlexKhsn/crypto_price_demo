package com.sanya.mg.sanyademo.api.asset.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class AssetPriceDto(
    @field:Schema(description = "Asset ID", example = "1")
    val id: Long,
    @field:Schema(description = "Base ticker symbol", example = "BTC")
    val baseTicker: String,
    @field:Schema(description = "Quote ticker symbol", example = "USDT")
    val quoteTicker: String,
    @field:Schema(description = "Quantity of the asset", example = "1.5")
    val quantity: BigDecimal,
    @field:Schema(description = "Current price", example = "13.234354")
    val currentPrice: BigDecimal,
    @field:Schema(description = "Asset total price", example = "433.34221")
    val totalPrice: BigDecimal = quantity * currentPrice,
){
    companion object {
        fun AssetPriceModel.toDto() = AssetPriceDto(
            id = this.id,
            baseTicker = this.baseTicker,
            quoteTicker = this.quoteTicker,
            quantity = this.quantity,
            currentPrice = this.currentPrice,
        )
    }
}
