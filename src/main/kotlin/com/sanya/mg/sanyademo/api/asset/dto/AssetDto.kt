package com.sanya.mg.sanyademo.api.asset.dto

import com.sanya.mg.sanyademo.repository.entity.Asset
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class AssetDto(
    @field:Schema(description = "Asset ID", example = "1")
    val id: Long,
    @field:Schema(description = "Base ticker symbol", example = "BTC")
    val baseTicker: String,
    @field:Schema(description = "Quote ticker symbol", example = "USDT")
    val quoteTicker: String,
    @field:Schema(description = "Quantity of the asset", example = "1.5")
    val quantity: BigDecimal,
    @field:Schema(description = "User ID", example = "1")
    val userId: Long,
) {
    companion object {
        infix fun fromEntity(entity: Asset) = AssetDto(
            id = entity.id!!,
            baseTicker = entity.baseTicker,
            quoteTicker = entity.quoteTicker,
            quantity = entity.quantity.stripTrailingZeros(),
            userId = entity.user.id!!,
        )
    }
}
