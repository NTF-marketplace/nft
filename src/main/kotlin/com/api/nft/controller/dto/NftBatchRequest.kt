package com.api.nft.controller.dto

import com.api.nft.enums.ChainType

data class NftBatchRequest(
    val tokenId: String,
    val tokenAddress: String,
    val chainType: ChainType,
)