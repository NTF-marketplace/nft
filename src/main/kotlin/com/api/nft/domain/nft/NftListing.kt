package com.api.nft.domain.nft

import com.api.nft.enums.ChainType
import com.api.nft.enums.StatusType
import com.api.nft.enums.TokenType
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("nft_listing")
data class NftListing(
    val id: Long,
    val price: BigDecimal,
    val chainType: ChainType,
    val nftId: Long,
    val statusType: StatusType,
    val createdDate: Long,
    val endDate: Long,
){
    fun updateStatus(statusType: StatusType): NftListing {
        return this.copy(statusType = statusType)
    }
}
