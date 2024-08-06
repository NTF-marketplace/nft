package com.api.nft.domain.attribute

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface AttributeRepository : ReactiveCrudRepository<Attribute,Long> {
}