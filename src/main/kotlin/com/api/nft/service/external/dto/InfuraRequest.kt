package com.api.nft.service.external.dto

data class InfuraRequest(
    val jsonrpc: String = "2.0",
    val method: String,
    val params: List<Any> = emptyList(),
    val id: Int = 1
)
