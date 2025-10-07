package com.sanya.mg.sanyademo.api.asset.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class AssetCreateRequest(
    @field:Schema(description = "Base ticker symbol", example = "BTC")
    @field:NotBlank(message = "Base ticker cannot be empty")
    val baseTicker: String,
    @field:Schema(description = "Quote ticker symbol", example = "USDT")
    @field:NotBlank(message = "Quote ticker cannot be empty")
    val quoteTicker: String,
    @field:Schema(description = "Quantity of the asset", example = "1.5")
    @field:DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    val quantity: BigDecimal,
    @field:Schema(description = "User ID", example = "1")
    @field:Positive(message = "User ID must be positive")
    val userId: Long,
)
