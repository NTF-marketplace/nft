package com.api.nft.domain.nft

import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import lombok.AllArgsConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

//TODO("캡슐화")
@Table("nft")
@AllArgsConstructor
class Nft(
    @Id val id: Long? = null,
    @Column("token_id") val tokenId: String,
    @Column("token_address") val tokenAddress: String,
    @Column("chain_type") val chinType: ChainType,
    @Column("contract_type") val contractType: ContractType,
    @Column("nft_name")val nftName: String,
    @Column("token_hash")val tokenHash: String?,
    val collectionName: String,
    val amount: Int,
) {
}