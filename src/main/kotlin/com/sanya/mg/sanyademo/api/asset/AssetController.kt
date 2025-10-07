package com.sanya.mg.sanyademo.api.asset

import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.api.asset.dto.AssetDto
import com.sanya.mg.sanyademo.api.asset.dto.AssetUpdateRequest
import com.sanya.mg.sanyademo.service.AssetService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
@Tag(name = "Assets", description = "Asset management API")
class AssetController(
    private val assetService: AssetService,
) {
    @Operation(summary = "Create new asset", description = "Creates a new asset for a user")
    @PostMapping
    fun create(
        @Valid
        @RequestBody
        request: AssetCreateRequest,
    ): ResponseEntity<AssetDto> {
        val new = assetService.createAsset(
            baseTicker = request.baseTicker,
            quoteTicker = request.quoteTicker,
            quantity = request.quantity,
            user = request.userId,
        )
        return ResponseEntity.status(201).body(new)
    }

    @Operation(summary = "Get asset by ID", description = "Returns a single asset by its ID")
    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long,
    ): ResponseEntity<AssetDto> {
        try {
            val result = assetService.getAssetById(id)
            return ResponseEntity.ok().body(result)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Get all assets", description = "Returns a list of all assets")
    @GetMapping
    fun getAll(): ResponseEntity<List<AssetDto>> {
        val result = assetService.getAllAssets()
        return ResponseEntity.ok().body(result)
    }

    @Operation(summary = "Get user assets", description = "Returns all assets belonging to a specific user")
    @GetMapping("/users/{userId}")
    fun getUserAssetsById(
        @PathVariable userId: Long,
    ): ResponseEntity<List<AssetDto>> {
        try {
            val assets = assetService.getUsersAssets(userId)
            return ResponseEntity.ok().body(assets)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Delete asset", description = "Deletes an asset by its ID")
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long,
    ): ResponseEntity<AssetDto> {
        try {
            val deleted = assetService.deleteAsset(id)
            return ResponseEntity.ok().body(deleted)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Update asset", description = "Updates the quantity of an asset")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody
        request: AssetUpdateRequest,
    ): ResponseEntity<AssetDto> {
        try {
            val updated = assetService.updateAsset(id, request.quantity)
            return ResponseEntity.ok().body(updated)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }
}
