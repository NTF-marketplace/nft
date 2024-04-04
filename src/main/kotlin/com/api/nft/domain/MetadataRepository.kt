package com.api.nft.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface MetadataRepository: ReactiveCrudRepository<Metadata,Long> {
}