package com.sanya.mg.sanyademo.api.asset

import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.api.asset.dto.AssetResponse
import com.sanya.mg.sanyademo.api.asset.dto.AssetUpdateRequest
import com.sanya.mg.sanyademo.service.AssetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/assets")
class AssetController(
    private val assetService: AssetService,
) {
    @PostMapping
    fun create(
        @RequestBody
        request: AssetCreateRequest,
    ): ResponseEntity<AssetResponse> {
        val new = assetService.createAsset(request)
        return ResponseEntity.status(201).body(new)
    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long,
    ): ResponseEntity<AssetResponse> {
        try {
            val result = assetService.getAssetById(id)
            return ResponseEntity.ok().body(result)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<AssetResponse>> {
        val result = assetService.getAllAssets()
        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<AssetResponse> {
        val deleted = assetService.deleteAsset(id)
        return ResponseEntity.ok().body(deleted)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody
        request: AssetUpdateRequest,
    ): ResponseEntity<AssetResponse> {
        try {
            val updated = assetService.updateAsset(id, request)
            return ResponseEntity.ok().body(updated)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }
}
