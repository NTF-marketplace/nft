package com.api.nft.service.dto

import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType

data class NftClientResponse(
    val id: Long,
    val tokenId: String,
    val tokenAddress: String,
    val chainType: ChainType,
    val contractType: ContractType,
    val nftName: String,
    val tokenHash: String,
)
