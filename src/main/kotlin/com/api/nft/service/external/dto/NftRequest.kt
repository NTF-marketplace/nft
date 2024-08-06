package com.api.nft.service.external.dto

import com.api.nft.enums.ChainType

data class NftRequest(
    val id: Long,
    val tokenAddress: String,
    val tokenId: String,
    val chainType: ChainType
)
