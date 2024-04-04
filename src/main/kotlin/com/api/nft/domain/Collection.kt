package com.api.nft.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("collection")
class Collection(
    @Id val name: String
) {
}