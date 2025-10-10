package com.sanya.mg.sanyademo.repository

import com.sanya.mg.sanyademo.repository.entity.AssetEntity
import com.sanya.mg.sanyademo.repository.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : JpaRepository<AssetEntity, Long> {
    fun findByBaseTickerAndQuoteTicker(baseTicker: String, quoteTicker: String): AssetEntity?

    fun findByUserIdAndBaseTickerAndQuoteTicker(userId: Long, baseTicker: String, quoteTicker: String): AssetEntity?

    fun getAllByUserId(userId: Long): MutableList<AssetEntity>

    fun getAssetsByUserId(userId: Long): MutableList<AssetEntity>
}
