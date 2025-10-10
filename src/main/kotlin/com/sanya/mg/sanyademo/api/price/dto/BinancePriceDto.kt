package com.sanya.mg.sanyademo.api.price.dto

import java.math.BigDecimal

data class BinancePriceDto(
    val symbol: String,
    val price: BigDecimal,
)
