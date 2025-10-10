package com.sanya.mg.sanyademo.api.asset.dto

import java.math.BigDecimal

data class AssetsTotalPriceDto(
    val userId: Long,
    val totalPrice: BigDecimal,
    val assetsPrices: List<AssetPriceDto>
)
