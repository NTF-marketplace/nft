package com.api.nft.domain.collection

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import lombok.AllArgsConstructor

@Table("collection")
@AllArgsConstructor
class Collection(
    @Id val name: String
) {
}