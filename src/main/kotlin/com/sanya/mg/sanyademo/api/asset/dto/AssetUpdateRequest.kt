package com.sanya.mg.sanyademo.api.asset.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

data class AssetUpdateRequest(
    @field:Schema(description = "New quantity of the asset", example = "2.5")
    val quantity: BigDecimal,
)
