package com.sanya.mg.sanyademo.api.asset.dto

import java.math.BigDecimal

data class AssetResponse(
    val id: Long,
    val baseTicker: String,
    val quoteTicker: String,
    val quantity: BigDecimal,
)
