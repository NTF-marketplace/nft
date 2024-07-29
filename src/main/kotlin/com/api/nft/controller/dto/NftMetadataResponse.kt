package com.api.nft.controller.dto

import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType


data class NftMetadataResponse(
    val id: Long,
    val tokenId: String,
    val tokenAddress: String,
    val contractType: ContractType,
    val chainType: ChainType,
    val nftName: String,
    val collectionName: String,
    val image: String,
    val lastPrice: Double?,
)
