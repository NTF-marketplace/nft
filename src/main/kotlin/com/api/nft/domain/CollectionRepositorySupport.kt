package com.api.nft.domain

import reactor.core.publisher.Mono

interface CollectionRepositorySupport {
    fun insert(collection: Collection) : Mono<Collection>
}