package com.api.nft.service.external

import com.api.nft.properties.MarketApiProperties
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MarketApiService(
    marketApiProperties: MarketApiProperties
) {
    private val webClient = WebClient.builder()
        .baseUrl(marketApiProperties.uri )
        .build()

    //TODO("")
    fun getOfferHistory(nftId: Long): Flux<Any> {
        return webClient.get()
            .uri{
                it.path("/v1/offer/history")
                    .queryParam("nftId", nftId)
                it.build()
            }
            .retrieve()
            .bodyToFlux(Any::class.java)
    }
    //TODO("")
    fun getLedgerHistory(nftId: Long): Flux<Any> {
        return webClient.get()
            .uri{
                it.path("/v1/orders/history")
                    .queryParam("nftId", nftId)
                it.build()
            }
            .retrieve()
            .bodyToFlux(Any::class.java)
    }
}