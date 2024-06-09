package com.api.nft.storage

import com.api.nft.enums.TokenType
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap

@Component
class PriceStorage {
    private val prices: ConcurrentHashMap<TokenType, BigDecimal> = ConcurrentHashMap()


    fun update(tokenType: TokenType, price: BigDecimal) {
        prices[tokenType] = price
    }

    fun get(tokenType: TokenType): BigDecimal? = prices[tokenType]
}