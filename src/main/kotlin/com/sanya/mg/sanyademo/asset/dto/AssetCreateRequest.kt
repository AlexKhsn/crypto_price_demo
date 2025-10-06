package com.sanya.mg.sanyademo.asset.dto

import java.math.BigDecimal

data class AssetCreateRequest(
    val baseTicker: String,
    val quoteTicker: String,
    val quantity: BigDecimal,
)
