package com.api.nft.service.dto

import com.api.nft.enums.StatusType
import com.api.nft.enums.TokenType
import java.math.BigDecimal

data class AuctionResponse(
    val id : Long,
    val nftId : Long,
    val address: String,
    val createdDateTime: Long,
    val endDateTime: Long,
    val statusType: StatusType,
    val startingPrice: BigDecimal,
    val tokenType: TokenType
)
