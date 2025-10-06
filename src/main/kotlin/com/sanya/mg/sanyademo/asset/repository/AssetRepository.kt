package com.sanya.mg.sanyademo.asset.repository

import com.sanya.mg.sanyademo.asset.repository.entity.Asset
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : JpaRepository<Asset, Long> {
    fun findByBaseTickerAndQuoteTicker(baseTicker: String, quoteTicker: String): Asset?
}
