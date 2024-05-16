package com.api.nft.service.dto

data class NftResponse(
    val id : Long,
    val tokenId: String,
    val tokenAddress: String,
    val chainType: String,
    val nftName: String,
    val collectionName: String
)
