package com.api.nft.domain.attribute

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface AttributeRepository : ReactiveCrudRepository<Attribute,Long> {

    fun findAllByNftId(nftId: Long): Flux<Attribute>
}