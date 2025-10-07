package com.sanya.mg.sanyademo.api.asset

import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.api.asset.dto.AssetDto
import com.sanya.mg.sanyademo.api.asset.dto.AssetUpdateRequest
import com.sanya.mg.sanyademo.service.AssetService
import com.sanya.mg.sanyademo.service.UserService
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
    private val userService: UserService,
) {
    @PostMapping
    fun create(
        @RequestBody
        request: AssetCreateRequest,
    ): ResponseEntity<AssetDto> {
        val user = userService.getUserById(request.userId)
        val new = assetService.createAsset(
            baseTicker = request.baseTicker,
            quoteTicker = request.quoteTicker,
            quantity = request.quantity,
            user = userService.getUserEntityById(user.id),
        )
        return ResponseEntity.status(201).body(new)
    }

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

    @GetMapping
    fun getAll(): ResponseEntity<List<AssetDto>> {
        val result = assetService.getAllAssets()
        return ResponseEntity.ok().body(result)
    }

    @GetMapping("/users/{id}")
    fun getUserAssetsById(
        @PathVariable id: Long,
    ): ResponseEntity<List<AssetDto>> {
        try {
            val user = userService.getUserEntityById(id)
            val assets = assetService.getUsersAssets(user)
            return ResponseEntity.ok().body(assets)
        } catch (e: NoSuchElementException) {
            return ResponseEntity.notFound().build()
        }
    }

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

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody
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
