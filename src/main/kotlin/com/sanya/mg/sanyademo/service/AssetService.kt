package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.dto.AssetResponse
import com.sanya.mg.sanyademo.dto.AssetUpdateRequest
import com.sanya.mg.sanyademo.entity.Asset
import com.sanya.mg.sanyademo.repository.AssetRepository
import org.springframework.stereotype.Service

@Service
class AssetService(
    private val repository: AssetRepository,
    val assetRepository: AssetRepository,
) {
    fun createAsset(request: AssetCreateRequest): AssetResponse {
        val forSave = Asset(
            id = null,
            baseTicker = request.baseTicker,
            quoteTicker = request.quoteTicker,
            quantity = request.quantity,
        )
        val saved = repository.save(forSave)
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

    fun updateAsset(id: Long, request: AssetUpdateRequest): AssetResponse {
        val found = assetRepository.findById(id).get()
        val updated = Asset(
            found.id!!,
            found.baseTicker,
            found.quoteTicker,
            request.quantity,
        )
        assetRepository.save(updated)
        return AssetResponse(
            updated.id!!,
            updated.baseTicker,
            updated.quoteTicker,
            updated.quantity,
        )
    }

    fun deleteAsset(id: Long): AssetResponse {
        val forDelete = assetRepository.findById(id).get()
        assetRepository.deleteById(id)
        return AssetResponse(
            forDelete.id!!,
            forDelete.baseTicker,
            forDelete.quoteTicker,
            forDelete.quantity,
        )
    }
}
