package com.sanya.mg.sanyademo.service

import com.sanya.mg.sanyademo.api.asset.dto.AssetPriceDto
import com.sanya.mg.sanyademo.api.price.dto.BinancePriceDto
import java.math.BigDecimal
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BinanceService {
    private val webClient = WebClient.builder()
        .baseUrl("https://api.binance.com")
        .build()

    fun getPrice(baseTicker: String, quoteTicker: String): BinancePriceDto? {
        val symbol = "$baseTicker$quoteTicker".uppercase()
        try {
            val response = webClient.get()
                .uri("/api/v3/ticker/price?symbol=$symbol")
                .retrieve()
                .bodyToMono<BinancePriceDto>()
                .block()

            return response
        } catch (e: Exception) {
            throw e
        }
    }
}
