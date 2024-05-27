package com.api.nft.domain.nft.repository

import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType


data class NftMetadataDto(
    val id: Long,
    val tokenId: String,
    val tokenAddress: String,
    val contractType: ContractType,
    val chainType: ChainType,
    val nftName: String,
    val collectionName: String,
    val image: String,
)
