package com.api.nft.service

import com.api.nft.domain.metadata.Metadata
import com.api.nft.domain.metadata.repository.MetadataRepository
import com.api.nft.service.external.dto.MetadataResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MetadataService(
    private val metadataRepository: MetadataRepository,
) {

    fun createMetadata(nftId: Long, metadata: MetadataResponse): Mono<Metadata> {
        return metadataRepository.save(
            Metadata(
                nftId = nftId,
                description = metadata.description,
                image = MetadataResponse.parseImage(metadata.image),
                animationUrl = metadata.animationUrl,
            )
        )

    }
}