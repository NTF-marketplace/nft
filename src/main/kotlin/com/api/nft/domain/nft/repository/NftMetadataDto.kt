package com.api.nft.domain.nft.repository


data class NftMetadataDto(
    val id: Long,
    val tokenId: String,
    val tokenAddress: String,
    val contractType: String,
    val chinType: String,
    val nftName: String,
    val collectionName: String,
    val image: String,
)
