package com.api.nft.domain.nft

import com.api.nft.enums.ChainType
import com.api.nft.enums.StatusType
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table("nft_auction")
data class NftAuction(
    val id: Long,
    val startingPrice: BigDecimal,
    val chainType: ChainType,
    val nftId: Long,
    val statusType: StatusType,
    val createdDate: Long,
    val endDate: Long,

) {
}