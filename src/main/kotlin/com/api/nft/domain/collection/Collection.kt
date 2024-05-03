package com.api.nft.domain.collection

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import lombok.AllArgsConstructor
import org.springframework.data.relational.core.mapping.Column

@Table("collection")
@AllArgsConstructor
class Collection(
    @Id val name: String,
    val logo: String?,
    @Column("banner_image")val bannerImage: String?,
    val description : String?,
) {
}