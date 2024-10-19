package com.api.nft.controller.dto

import com.api.nft.enums.OrderType
import java.math.BigDecimal

data class OrdersResponse(
    val id : Long,
    val orderType: OrderType,
    val address: String,
    val ledgerPrice: BigDecimal,
    val createdAt: Long?,
)
