package com.api.nft.domain.nft

import com.api.nft.enums.TokenType
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

//1:1 매핑으로?
@Table("nft_listing")
data class NftListing(
    val id: Long? = null,
    val price: BigDecimal,
    val tokenType: TokenType,
    val nftId: Long,
)
