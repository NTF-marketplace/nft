package com.api.nft.service.external.dto

data class EthLogRequest(
    val fromBlock: String,
    val toBlock: String,
    val address: String,
    val topics: List<Any?>,
)

