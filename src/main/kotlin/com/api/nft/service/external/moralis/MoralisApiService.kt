package com.api.nft.service.external.moralis

import com.api.nft.enums.ChainType
import com.api.nft.properties.ApiKeysProperties
import com.api.nft.service.external.dto.NFTByWalletResponse
import com.api.nft.service.external.dto.NftData
import com.api.nft.service.external.dto.NftTransferData
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class MoralisApiService(
    private val apiKeysProperties: ApiKeysProperties,
) {

    private val webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build()

    private fun queryParamByChain(chain: ChainType): String {
        val chain = when (chain) {
            ChainType.ETHEREUM_MAINNET -> "0x1"
            ChainType.POLYGON_MAINNET -> "0x89"
            ChainType.LINEA_MAINNET -> "0xe708"
            ChainType.LINEA_SEPOLIA -> "0xe705"
            ChainType.ETHEREUM_HOLESKY -> "0x4268"
            ChainType.ETHEREUM_SEPOLIA -> "0xaa36a7"
            ChainType.POLYGON_AMOY -> "0x13882"
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
            .header("X-API-Key", apiKeysProperties.moralis)
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(NftTransferData::class.java)
    }

    fun getNFTsByAddress(walletAddress: String,chainType: ChainType): Mono<NFTByWalletResponse> {
        val chain = queryParamByChain(chainType)
        return webClient.get()
            .uri {
                it.path("/v2.2/${walletAddress}/nft")
                it.queryParam("chain", chain)
                it.queryParam("exclude_spam", true)
                it.build()
            }
            .header("X-API-Key", apiKeysProperties.moralis)
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(NFTByWalletResponse::class.java)
    }

    fun getNFTMetadata(tokenAddress: String,tokenId:String,chainType: ChainType): Mono<NftData> {
        val chain = queryParamByChain(chainType)
        return webClient.get()
            .uri {
                it.path("/v2.2/nft/${tokenAddress}/${tokenId}")
                it.queryParam("chain", chain)
                it.build()
            }
            .header("X-API-Key", apiKeysProperties.moralis)
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(NftData::class.java)
    }



    companion object {
        private val baseUrl = "https://deep-index.moralis.io/api"
    }
}