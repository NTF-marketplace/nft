package com.api.nft.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CollectionRepository : ReactiveCrudRepository<Collection,String>, CollectionRepositorySupport {

    fun findByName(name: String): Mono<Collection>
}