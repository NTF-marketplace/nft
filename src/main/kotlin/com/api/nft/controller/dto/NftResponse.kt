package com.api.nft.controller.dto

data class NftResponse(
    val id: Long,
    val collectionName: String,
    val tokenId: String,
    val tokenAddress: String,
    val image: String, // metadata
    val value: String, // attribute
)
