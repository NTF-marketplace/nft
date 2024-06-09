package com.api.nft.domain.nft

import com.api.nft.enums.TokenType
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("nft_listing")
data class NftListing(
    val id: Long? = null,
    val price: BigDecimal,
    val tokenType: TokenType,
    val nftId: Long,
){
    fun update(newPrice: BigDecimal,new_tokenType: TokenType): NftListing {
        return this.copy(price = newPrice, tokenType = new_tokenType)
    }
}
