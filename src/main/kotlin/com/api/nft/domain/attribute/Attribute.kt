package com.api.nft.domain.attribute

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import lombok.AllArgsConstructor

@Table("attribute")
@AllArgsConstructor
class Attribute(
    @Id val id: Long? = null,
    val nftId : Long,
    val traitType : String? = null,
    val value: String? = null,
) {

}