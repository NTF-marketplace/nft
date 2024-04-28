package com.api.nft.domain.trasfer

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("transfer")
class Transfer(
    @Id val id: Long? = null,
    @Column("nft_id") val nftId: Long,
    @Column("from_address") val fromAddress: String,
    @Column("to_address") val toAddress: String,
    ) {


}