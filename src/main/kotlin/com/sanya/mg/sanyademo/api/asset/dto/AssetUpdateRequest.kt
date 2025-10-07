package com.sanya.mg.sanyademo.api.asset.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

data class AssetUpdateRequest(
    @field:Schema(description = "New quantity of the asset", example = "2.5")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    val quantity: BigDecimal,
)
