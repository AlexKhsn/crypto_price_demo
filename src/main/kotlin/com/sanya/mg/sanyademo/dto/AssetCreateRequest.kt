package com.sanya.mg.sanyademo.dto

import java.math.BigDecimal

data class AssetCreateRequest(
    val baseTicker: String,
    val quoteTicker: String,
    val quantity: BigDecimal,
)
