package com.api.nft.service

import java.math.BigInteger

data class TransferResponse(
    val from: String,
    val to: String,
    val tokenId: BigInteger
)
