package com.api.nft.service.api

import com.api.nft.enums.ChainType
import com.api.nft.properties.ApiKeysProperties
import com.api.nft.service.TransferResponse
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.math.BigInteger


@Service
class TransferService(
    private val apiKeysProperties: ApiKeysProperties,
) {

     fun createTransfer() {
       TODO()
    }

    private fun getFullUrl(chainType: ChainType): String {
        val chainUrl = when (chainType) {
            ChainType.ETHEREUM_MAINNET -> "mainnet.infura.io"
            ChainType.POLYGON_MAINNET -> "polygon-mainnet.infura.io"
            ChainType.ETHREUM_GOERLI -> "goerli.infura.io"
            ChainType.ETHREUM_SEPOLIA -> "sepolia.infura.io"
            ChainType.POLYGON_MUMBAI -> "polygon-mumbai.infura.io"
        }
        return "https://$chainUrl/v3/${apiKeysProperties.infura}"
    }

    private fun getWeb3j(chainType: ChainType): Web3j {
        return Web3j.build(HttpService(getFullUrl(chainType)))
    }


    fun getTransactionERC721(chainType: ChainType, contractAddress: String, tokenId: String): List<TransferResponse> {
        val web3 = getWeb3j(chainType)
        val transferEvent = Event(
            "Transfer",
            listOf(
                TypeReference.create(Address::class.java, true),
                TypeReference.create(Address::class.java, true),
                TypeReference.create(Uint256::class.java, true)
            )
        )

        val eventSignature = EventEncoder.encode(transferEvent)

        val filter = EthFilter(
            DefaultBlockParameterName.EARLIEST,
            DefaultBlockParameterName.LATEST,
            contractAddress
        ).addSingleTopic(eventSignature)
            .addSingleTopic(null)
            .addSingleTopic(null)
            .addSingleTopic(Numeric.toHexStringWithPrefixZeroPadded(BigInteger(tokenId), 64))

        val responses = mutableListOf<TransferResponse>()

        try {
            val logs = web3.ethGetLogs(filter).send()
            logs.logs.forEach {
                val log = it.get() as Log
                val (from, to, token) = parseTransfer(log)
                responses.add(TransferResponse(from, to, token))
            }
        } catch (e: Exception) {
            println("Error fetching logs: ${e.message}")
        }

        return responses
    }

    private fun parseTransfer(log: Log): Triple<String, String, BigInteger> {
        val fromAddress = "0x" + log.topics[1].substring(log.topics[1].length - 40)
        val toAddress = "0x" + log.topics[2].substring(log.topics[2].length - 40)
        val tokenId = Numeric.toBigInt(log.topics[3])
        return Triple(fromAddress, toAddress, tokenId)
    }
}

