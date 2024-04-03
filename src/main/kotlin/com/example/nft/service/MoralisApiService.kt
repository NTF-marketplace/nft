package com.example.nft.service

import com.example.nft.enums.ChainType
import com.example.nft.service.dto.NftResponse

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MoralisApiService {

    private val webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build()

    private fun queryParamByChain(chain: ChainType): String? {
        val chain = when (chain) {
            ChainType.ETHEREUM_MAINNET -> "eth"
            ChainType.POLYGON_MAINNET -> "polygon"
            ChainType.ETHREUM_GOERLI -> "goerli"
            ChainType.POLYGON_MUMBAI -> "munbai"
            ChainType.ETHREUM_SEPOLIA -> "sepolia"
        }
        return chain
    }

    fun getNft(tokenAddress: String,tokenId: String,chainType: ChainType): Mono<NftResponse> {
        val chain = queryParamByChain(chainType)

        return webClient.get()
            .uri {
                it.path("/v2.2/nft/${tokenAddress}/${tokenId}")
                it.queryParam("chain", chain)
                it.build()
            }
            .header("X-API-Key", apiKey)
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(NftResponse::class.java)
    }

    companion object {
        private val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6ImJiNmIxMWJmLWNmNzItNDg0OC04OGEyLTBjYTIwODRjN2VhMyIsIm9yZ0lkIjoiMzgzODQwIiwidXNlcklkIjoiMzk0NDAyIiwidHlwZUlkIjoiMGZlYWQ5NDctZjQwZS00MDkwLWFlNGUtOTA1ZTdmMjUxZTAzIiwidHlwZSI6IlBST0pFQ1QiLCJpYXQiOjE3MTA5NTIwMjYsImV4cCI6NDg2NjcxMjAyNn0.VQE60IPGiWxdp7jKLF0jzXnxrLjEpU56H4bnfhMt0Sw"
        private val baseUrl = "https://deep-index.moralis.io/api"
    }
}