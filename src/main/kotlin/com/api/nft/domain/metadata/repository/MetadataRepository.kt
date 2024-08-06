package com.api.nft.domain.metadata.repository

import com.api.nft.domain.metadata.Metadata
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface MetadataRepository: ReactiveCrudRepository<Metadata,Long> {
}