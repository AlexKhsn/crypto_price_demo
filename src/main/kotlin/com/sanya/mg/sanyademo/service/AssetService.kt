package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.asset.dto.AssetDto
import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.AssetRepository
import com.sanya.mg.sanyademo.repository.entity.Asset
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

@Service
class AssetService(
    private val assetRepository: AssetRepository,
    val transactionService: TransactionService,
    private val userService: UserService,
) {
    @Transactional
    fun createAsset(
        baseTicker: String,
        quoteTicker: String,
        quantity: BigDecimal,
        user: Long,
    ): AssetDto {
        val user = userService.getUserEntityById(user)

        val existingAsset = assetRepository.findByUserAndBaseTickerAndQuoteTicker(user, baseTicker, quoteTicker)

        if (existingAsset != null) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Asset already exists")

        val forSave = Asset(
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

        return AssetDto fromEntity saved
    }

    fun getAssetById(id: Long): AssetDto {
        val found = assetRepository.findById(id).get()
        return AssetDto fromEntity found
    }

    fun getAllAssets(): List<AssetDto> {
        val found = assetRepository.findAll()
        return found.map { asset -> AssetDto fromEntity asset }
    }

    @Transactional
    fun updateAsset(id: Long, quantity: BigDecimal): AssetDto {
        val found = assetRepository.findById(id).get()
        val updated = Asset(
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
    fun deleteAsset(id: Long): AssetDto {
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

    @Transactional
    fun getUsersAssets(userId: Long): List<AssetDto> {
        val user = userService.getUserEntityById(userId)
        return user.assets.map { asset -> AssetDto.fromEntity(asset) }
    }
}
