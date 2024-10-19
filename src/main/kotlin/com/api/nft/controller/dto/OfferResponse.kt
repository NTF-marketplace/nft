package com.api.nft.controller.dto

import java.math.BigDecimal

data class OfferResponse(
    val id: Long,
    val address: String,
    val createdAt: Long?,
    val price: BigDecimal,
)
