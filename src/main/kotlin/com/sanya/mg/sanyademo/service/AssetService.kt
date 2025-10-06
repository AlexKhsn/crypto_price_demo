package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.api.asset.dto.AssetResponse
import com.sanya.mg.sanyademo.api.asset.dto.AssetUpdateRequest
import com.sanya.mg.sanyademo.api.transaction.dto.CreateTransactionDto
import com.sanya.mg.sanyademo.common.TransactionType
import com.sanya.mg.sanyademo.repository.AssetRepository
import com.sanya.mg.sanyademo.repository.entity.Asset
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AssetService(
    private val assetRepository: AssetRepository,
    val transactionService: TransactionService,
) {
    @Transactional
    fun createAsset(request: AssetCreateRequest): AssetResponse {
        val forSave = Asset(
            id = null,
            baseTicker = request.baseTicker,
            quoteTicker = request.quoteTicker,
            quantity = request.quantity,
        )
        val saved = assetRepository.save(forSave)

        transactionService.createTransaction(
            CreateTransactionDto(
                type = TransactionType.BUY,
                symbol = saved.baseTicker,
                quantity = saved.quantity,
            ),
        )

        return AssetResponse(
            saved.id!!,
            saved.baseTicker,
            saved.quoteTicker,
            saved.quantity,
        )
    }

    fun getAssetById(id: Long): AssetResponse {
        val found = assetRepository.findById(id).get()
        return AssetResponse(
            found.id!!,
            found.baseTicker,
            found.quoteTicker,
            found.quantity,
        )
    }

    fun getAllAssets(): List<AssetResponse> {
        val found = assetRepository.findAll()
        return found.map { asset ->
            AssetResponse(
                asset.id!!,
                asset.baseTicker,
                asset.quoteTicker,
                asset.quantity,
            )
        }
    }

    @Transactional
    fun updateAsset(id: Long, request: AssetUpdateRequest): AssetResponse {
        val found = assetRepository.findById(id).get()
        val updated = Asset(
            found.id!!,
            found.baseTicker,
            found.quoteTicker,
            request.quantity,
        )

        if(updated.quantity > found.quantity) {
            transactionService.createTransaction(
                CreateTransactionDto(
                    type = TransactionType.BUY,
                    symbol = updated.baseTicker,
                    quantity = updated.quantity - found.quantity,
                )
            )
        } else if(updated.quantity < found.quantity) {
            transactionService.createTransaction(
                CreateTransactionDto(
                    type = TransactionType.SELL,
                    symbol = updated.baseTicker,
                    quantity = found.quantity - updated.quantity,
                )
            )
        }

        val saved = assetRepository.save(updated)


        return AssetResponse(
            updated.id!!,
            updated.baseTicker,
            updated.quoteTicker,
            updated.quantity,
        )
    }

    @Transactional
    fun deleteAsset(id: Long): AssetResponse {
        val forDelete = assetRepository.findById(id).get()

        transactionService.createTransaction(
            CreateTransactionDto(
                type = TransactionType.SELL,
                symbol = forDelete.baseTicker,
                quantity = forDelete.quantity,
            ),
        )

        assetRepository.deleteById(id)


        return AssetResponse(
            forDelete.id!!,
            forDelete.baseTicker,
            forDelete.quoteTicker,
            forDelete.quantity,
        )
    }
}
