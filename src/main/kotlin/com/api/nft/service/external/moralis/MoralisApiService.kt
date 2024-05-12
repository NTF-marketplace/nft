package com.api.nft.service.external.moralis

import com.api.nft.enums.ChainType
import com.api.nft.service.external.dto.NftData
import com.api.nft.service.external.dto.NftTransferData

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
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

    fun getNftTransfer(tokenAddress: String,
                       tokenId: String,
                       chainType: ChainType
    ): Mono<NftTransferData>{
        val chain = queryParamByChain(chainType)

        return webClient.get()
            .uri {
                it.path("/v2.2/nft/${tokenAddress}/${tokenId}/transfers")
                it.queryParam("chain", chain)
                it.build()
            }
            .header("X-API-Key", apiKey)
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(NftTransferData::class.java)
    }



    companion object {
        private val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub25jZSI6ImJiNmIxMWJmLWNmNzItNDg0OC04OGEyLTBjYTIwODRjN2VhMyIsIm9yZ0lkIjoiMzgzODQwIiwidXNlcklkIjoiMzk0NDAyIiwidHlwZUlkIjoiMGZlYWQ5NDctZjQwZS00MDkwLWFlNGUtOTA1ZTdmMjUxZTAzIiwidHlwZSI6IlBST0pFQ1QiLCJpYXQiOjE3MTA5NTIwMjYsImV4cCI6NDg2NjcxMjAyNn0.VQE60IPGiWxdp7jKLF0jzXnxrLjEpU56H4bnfhMt0Sw"
        private val baseUrl = "https://deep-index.moralis.io/api"
    }
}