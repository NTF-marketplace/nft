package com.api.nft.domain.nft.repository

import org.springframework.data.relational.core.mapping.Column

data class NftMetadataDto(
    val id: Long,
    val tokenId: String,
    val tokenAddress: String,
    val chinType: String,
    val nftName: String,
    val collectionName: String,
    val image: String,
)
