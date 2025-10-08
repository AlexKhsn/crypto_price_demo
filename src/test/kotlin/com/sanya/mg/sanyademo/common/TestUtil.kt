package com.sanya.mg.sanyademo.common

import com.sanya.mg.sanyademo.api.asset.dto.AssetCreateRequest
import com.sanya.mg.sanyademo.api.asset.dto.AssetDto
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

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
}
