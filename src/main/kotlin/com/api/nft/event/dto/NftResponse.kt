package com.api.nft.event.dto

import com.api.nft.enums.ChainType

data class NftResponse(
    val id : Long,
    val tokenId: String,
    val tokenAddress: String,
    val chainType: ChainType,
    val nftName: String,
    val collectionName: String
)
