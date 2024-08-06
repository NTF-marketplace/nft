package com.api.nft.service.external.dto

import java.math.BigDecimal

data class BinanceTickerPriceResponse(
    val symbol : String,
    val price: BigDecimal,
)
