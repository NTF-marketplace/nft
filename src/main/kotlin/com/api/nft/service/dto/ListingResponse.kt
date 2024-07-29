package com.api.nft.service.dto

import com.api.nft.enums.ChainType
import com.api.nft.enums.StatusType
import java.math.BigDecimal

data class ListingResponse(
    val id : Long,
    val nftId : Long,
    val address: String,
    val createdDateTime: Long,
    val endDateTime: Long,
    val statusType: StatusType,
    val price: BigDecimal,
    val chainType: ChainType
)