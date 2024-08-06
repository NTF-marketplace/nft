package com.api.nft.service.external.dto

data class InfuraResponse(
    val jsonrpc: String,
    val id: Int,
    val result: List<Map<String, Any>>,
) {

        fun toEthLogResponses(): List<EthLogResponse> {
            return result.map { EthLogResponse.toResponse(it) }
        }

}

