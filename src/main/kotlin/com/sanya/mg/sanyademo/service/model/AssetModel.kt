package com.sanya.mg.sanyademo.service.model

import com.sanya.mg.sanyademo.repository.entity.AssetEntity
import java.math.BigDecimal

data class AssetModel(
    val id: Long,
    val baseTicker: String,
    val quoteTicker: String,
    val quantity: BigDecimal,
){
    companion object {
        fun AssetEntity.toModel() = AssetModel(
            id = this.id!!,
            baseTicker = this.baseTicker,
            quoteTicker = this.quoteTicker,
            quantity = this.quantity,
        )
    }
}
