package com.api.nft.domain.metadata

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import lombok.AllArgsConstructor

@Table("metadata")
@AllArgsConstructor
class Metadata(
    @Id val id: Long? = null,
    @Column("nft_id") val nftId: Long,
    val description: String?,
    val image: String?,
    @Column("animation_url") val animationUrl: String?,
) {
}