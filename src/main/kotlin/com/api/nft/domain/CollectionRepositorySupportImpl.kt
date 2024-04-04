package com.api.nft.domain

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Mono

class CollectionRepositorySupportImpl(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
) : CollectionRepositorySupport {
    override fun insert(collection: Collection): Mono<Collection> {
        return r2dbcEntityTemplate.insert(collection)
    }
}