package com.api.nft.kafka.dto

import com.api.nft.enums.ChainType
import com.api.nft.enums.StatusType
import com.api.nft.enums.OrderType
import java.math.BigDecimal

data class SaleResponse(
    val id : Long,
    val nftId : Long,
    val address: String,
    val createdDateTime: Long,
    val endDateTime: Long,
    val statusType: StatusType,
    val price: BigDecimal,
    val chainType: ChainType,
    val orderType: OrderType
)


