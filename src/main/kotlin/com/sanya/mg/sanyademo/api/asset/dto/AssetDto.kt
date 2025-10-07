package com.sanya.mg.sanyademo.api.asset.dto

import com.sanya.mg.sanyademo.repository.entity.Asset
import java.math.BigDecimal

data class AssetDto(
    val id: Long,
    val baseTicker: String,
    val quoteTicker: String,
    val quantity: BigDecimal,
    val userId: Long,
) {
    companion object {
        infix fun fromEntity(entity: Asset) = AssetDto(
            id = entity.id!!,
            baseTicker = entity.baseTicker,
            quoteTicker = entity.quoteTicker,
            quantity = entity.quantity,
            userId = entity.user.id!!,
        )
    }
}
