package com.sanya.mg.sanyademo.repository

import com.sanya.mg.sanyademo.repository.entity.Asset
import com.sanya.mg.sanyademo.repository.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : JpaRepository<Asset, Long> {
    fun findByBaseTickerAndQuoteTicker(baseTicker: String, quoteTicker: String): Asset?

    fun findByUserAndBaseTickerAndQuoteTicker(user: User, baseTicker: String, quoteTicker: String): Asset?
}
