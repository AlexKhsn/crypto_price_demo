package com.sanya.mg.sanyademo.binance.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Service
class BinanceService {
    private val webClient = WebClient.builder()
        .baseUrl("https://api.binance.com")
        .build()

    fun getPrice(baseTicker: String, quoteTicker: String): String {
        val symbol = "$baseTicker$quoteTicker".uppercase()
        val response = webClient.get()
            .uri("/api/v3/ticker/price?symbol=$symbol")
            .retrieve()
            .bodyToMono<String>()
            .block()

        return response ?: "Unable to fetch price"
    }
}
