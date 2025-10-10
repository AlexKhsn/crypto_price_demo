package com.sanya.mg.sanyademo.api.price

import com.sanya.mg.sanyademo.api.price.dto.BinancePriceDto
import com.sanya.mg.sanyademo.service.BinanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/binance")
@Deprecated("only for debug usage")
class BinancePriceController(
    private val binanceService: BinanceService,
) {
    @Deprecated("only for debug usage")
    @GetMapping("/price/{baseTicker}/{quoteTicker}")
    fun getPrice(
        @PathVariable baseTicker: String,
        @PathVariable quoteTicker: String,
    ): ResponseEntity<BinancePriceDto> {
        val price = binanceService.getPrice(baseTicker, quoteTicker)
        println("$baseTicker-$quoteTicker price: $price")
        return ResponseEntity.ok().body(price)
    }
}
