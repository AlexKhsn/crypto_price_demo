package com.sanya.mg.sanyademo.common

import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.api.asset.dto.AssetDto
import com.sanya.mg.sanyademo.repository.entity.User
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalDateTime.now

object TestUtil {
    infix fun AssetDto?.shouldMatch(request: AssetCreateRequest?) {
        this shouldNotBe null
        this!!
        request shouldNotBe null
        request!!
        this.baseTicker shouldBe request.baseTicker
        this.quoteTicker shouldBe request.quoteTicker
        this.quantity shouldBe request.quantity
        this.userId shouldBe request.userId
        this.id shouldBeGreaterThan 0
    }

    fun createDefaultUser(
        id: Long? = null,
        username: String = "Test User",
        email: String = "jonh.doe@example.com",
        createdAt: LocalDateTime = now(),
    ): User {
        return User(
            id = id,
            username = username,
            email = email,
            createdAt = createdAt,
        )
    }

    fun createDefaultAssetRequest(
        baseTicker: String = "BTC",
        quoteTicker: String = "USDT",
        quantity: Double = 1.5,
        userId: Long = 1,
    ): AssetCreateRequest {
        return AssetCreateRequest(
            baseTicker = baseTicker,
            quoteTicker = quoteTicker,
            quantity = BigDecimal(quantity),
            userId = userId,
        )
    }
}
