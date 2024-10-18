package com.api.nft.service.dto

import java.math.BigInteger

data class TransferResponse(
    val from: String,
    val to: String,
    val tokenId: BigInteger
)
