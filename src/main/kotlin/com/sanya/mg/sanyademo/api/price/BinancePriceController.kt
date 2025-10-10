package com.sanya.mg.sanyademo.api.price

//import com.sanya.mg.sanyademo.service.BinanceService
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/api/binance")
//class BinancePriceController(
//    private val binanceService: BinanceService,
//) {
//    @GetMapping("/price/{baseTicker}/{quoteTicker}")
//    fun getPrice(
//        @PathVariable baseTicker: String,
//        @PathVariable quoteTicker: String,
//    ): ResponseEntity<String> {
//        val price = binanceService.getPrice(baseTicker, quoteTicker)
//        println("$baseTicker-$quoteTicker price: $price")
//        return ResponseEntity.ok().body(price)
//    }
//}
