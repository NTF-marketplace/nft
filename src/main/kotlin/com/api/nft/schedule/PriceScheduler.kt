package com.api.nft.schedule

import com.api.nft.enums.TokenType
import com.api.nft.service.external.binance.BinanceApiService
import com.api.nft.storage.PriceStorage
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PriceScheduler(
    private val priceStorage: PriceStorage,
    private val binanceApiService: BinanceApiService,
    )
{
    @Scheduled(fixedRate = 3600000)
    fun updatePrices() {
        TokenType.entries.map {
            binanceApiService.getTickerPrice(it)
                .subscribe { response->
                    priceStorage.update(it,response.price)
                    println("Updated ${it} price to ${response.price}")
                }
        }
    }

}