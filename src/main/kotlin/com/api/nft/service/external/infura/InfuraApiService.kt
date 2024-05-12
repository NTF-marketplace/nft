package com.api.nft.service.external.infura

import com.api.nft.enums.ChainType
import com.api.nft.service.external.dto.EthLogRequest
import com.api.nft.service.external.dto.EthLogResponse
import com.api.nft.service.external.dto.InfuraRequest
import com.api.nft.service.external.dto.InfuraResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.utils.Numeric
import reactor.core.publisher.Mono
import java.math.BigInteger
import java.time.Instant


@Service
class InfuraApiService {

    private fun urlByChain(chainType: ChainType) : WebClient {
        val baseUrl = when (chainType) {
            ChainType.ETHEREUM_MAINNET -> "https://mainnet.infura.io"
            ChainType.POLYGON_MAINNET -> "https://polygon-mainnet.infura.io"
            ChainType.ETHREUM_GOERLI -> "https://goerli.infura.io"
            ChainType.ETHREUM_SEPOLIA -> "https://sepolia.infura.io"
            ChainType.POLYGON_MUMBAI -> "https://polygon-mumbai.infura.io"
        }
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build()
    }


    fun getEthLogs(
        fromBlock: String,
        chainType: ChainType,
        tokenAddress: String,
        tokenId: String,
    ): Mono<List<EthLogResponse>>{

        val transferEvent = Event(
            "Transfer",
            listOf(
                TypeReference.create(Address::class.java, true),
                TypeReference.create(Address::class.java, true),
                TypeReference.create(Uint256::class.java, true),
            )
        )

        val eventSignature = EventEncoder.encode(transferEvent)
        val topicTokenId = Numeric.toHexStringWithPrefixZeroPadded(BigInteger(tokenId), 64)
        val toHexFromBlock = Numeric.toHexStringWithPrefix(BigInteger(fromBlock))


        val requestBody = InfuraRequest(
            method = "eth_getLogs",
            params = listOf(EthLogRequest(
                toHexFromBlock,
                "latest",
                tokenAddress,
                listOf(eventSignature, null, null, topicTokenId)))
        )
            val webClient = urlByChain(chainType)

            return webClient.post()
                .uri("/v3/$apiKey")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(InfuraResponse::class.java)
                .mapNotNull { it.toEthLogResponses() }

        }

    fun getBlockTimestamp(blockNumber: String, chainType: ChainType): Mono<Long> {
        val webClient = urlByChain(chainType)
        val requestBody = InfuraRequest(
            method = "eth_getBlockByNumber",
            params = listOf(blockNumber, false),
        )

        return webClient.post()
            .uri("/v3/$apiKey")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map::class.java)
            .mapNotNull { response ->
                val result = response["result"] as Map<String, Any>
                val timestamp = result["timestamp"].toString()
                Instant.ofEpochSecond(Numeric.decodeQuantity(timestamp).longValueExact()).toEpochMilli()
            }
    }

    companion object{
        private val apiKey = "98b672d2ce9a4089a3a5cb5081dde2fa"
    }
}