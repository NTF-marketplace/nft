package com.api.nft.domain.metadata.repository

import com.api.nft.domain.metadata.Metadata
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface MetadataRepository: ReactiveCrudRepository<Metadata,Long> {

    fun findByNftId(nftId: Long) : Mono<Metadata>
}