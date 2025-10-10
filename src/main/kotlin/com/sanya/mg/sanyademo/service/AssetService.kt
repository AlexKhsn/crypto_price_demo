package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.asset.dto.AssetDto
import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.AssetRepository
import com.sanya.mg.sanyademo.repository.entity.AssetEntity
import com.sanya.mg.sanyademo.service.model.AssetModel
import com.sanya.mg.sanyademo.service.model.AssetModel.Companion.toModel
import java.math.BigDecimal
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AssetService(
    private val assetRepository: AssetRepository,
    private val transactionService: TransactionService,
    private val userService: UserService,
    private val binanceService: BinanceService,
) {
    @Transactional
    fun createAsset(
        baseTicker: String,
        quoteTicker: String,
        quantity: BigDecimal,
        userId: Long,
    ): AssetModel {
        val user = userService.getUserEntityById(userId)

        val existingAsset = assetRepository.findByUserIdAndBaseTickerAndQuoteTicker(user.id!!, baseTicker, quoteTicker)

        if (existingAsset != null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset already exists")

        val forSave = AssetEntity(
            id = null,
            baseTicker = baseTicker,
            quoteTicker = quoteTicker,
            quantity = quantity,
            user = user,
        )
        val saved = assetRepository.save(forSave)

        transactionService.createTransaction(
            type = TransactionType.BUY,
            symbol = saved.baseTicker,
            quantity = saved.quantity,
            user = saved.user,
        )

        return saved.toModel()
    }

    fun getAssetById(id: Long): AssetModel {
        val found = assetRepository.findById(id).get()
        return AssetDto fromEntity found
    }

    fun getAllAssets(): List<AssetModel> {
        val found = assetRepository.findAll()
        return found.map { asset -> AssetDto fromEntity asset }
    }

    @Transactional
    fun updateAsset(id: Long, quantity: BigDecimal): AssetModel {
        val found = assetRepository.findById(id).get()
        val updated = AssetEntity(
            found.id!!,
            found.baseTicker,
            found.quoteTicker,
            quantity,
            found.user,
        )

        if (updated.quantity > found.quantity) {
            transactionService.createTransaction(
                type = TransactionType.BUY,
                symbol = updated.baseTicker,
                quantity = updated.quantity - found.quantity,
                user = updated.user,
            )
        } else if (updated.quantity < found.quantity) {
            transactionService.createTransaction(
                type = TransactionType.SELL,
                symbol = updated.baseTicker,
                quantity = found.quantity - updated.quantity,
                user = updated.user,
            )
        }

        val saved = assetRepository.save(updated)

        return AssetDto fromEntity saved
    }

    @Transactional
    fun deleteAsset(id: Long): AssetModel {
        val forDelete = assetRepository.findById(id).get()

        transactionService.createTransaction(
            type = TransactionType.SELL,
            symbol = forDelete.baseTicker,
            quantity = forDelete.quantity,
            user = forDelete.user,
        )

        assetRepository.deleteById(id)

        return AssetDto fromEntity forDelete
    }

    @Transactional(readOnly = true)
    fun getUsersAssets(userId: Long): List<AssetModel> {
        val user = userService.getUserEntityById(userId)
        return user.assets.map { asset -> AssetDto.fromEntity(asset) }
    }

    fun getAssetPrice(assetId: Long): AssetPriceModel {
        try {
            val asset = assetRepository.findById(assetId).get().toModel()
            val currentPrice = binanceService.getPrice(asset.baseTicker, asset.quoteTicker)
            return AssetModel(
                asset.id!!,
                asset.baseTicker,
                asset.quoteTicker,
                asset.quantity,
                currentPrice!!.price,
            )
        } catch (e: Exception) {
            throw e
        }
    }

    fun getUsersAssetsTotalPrice(userId: Long): List<AssetPriceModel> {
        try {
            val userAssetsPricesDtos = assetRepository.getAssetsByUserId(userId)
                .map { asset -> getAssetPrice(asset.id!!) }

            return userAssetsPricesDtos
        } catch (e: Exception) {
            throw e
        }
    }
}
