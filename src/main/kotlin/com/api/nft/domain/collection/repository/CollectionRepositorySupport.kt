package com.api.nft.domain.collection.repository

import com.api.nft.domain.collection.Collection
import reactor.core.publisher.Mono

interface CollectionRepositorySupport {
    fun insert(collection: Collection) : Mono<Collection>
}