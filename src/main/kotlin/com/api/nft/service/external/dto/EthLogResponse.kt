package com.api.nft.service.external.dto

import com.api.nft.domain.trasfer.Transfer
import java.math.BigInteger

data class EthLogResponse(
    val address: String,
    val blockHash: String,
    val blockNumber: String,
    val data: String,
    val logIndex: String,
    val topics: List<String>,
    val transactionHash: String,
    val transactionIndex: String,
){
    companion object{

        fun toResponse(map: Map<String, Any>): EthLogResponse {
            return EthLogResponse(
                address = map["address"] as String,
                blockHash = map["blockHash"] as String,
                blockNumber = map["blockNumber"] as String,
                data = map["data"] as String,
                logIndex = map["logIndex"] as String,
                topics = map["topics"] as List<String>,
                transactionHash = map["transactionHash"] as String,
                transactionIndex = map["transactionIndex"] as String
            )
        }
    }

    private fun parseAddress(address: String): String {
        return "0x" + address.substring(26).padStart(40, '0')
    }
}

