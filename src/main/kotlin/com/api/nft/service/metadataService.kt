package com.api.nft.service

import com.api.nft.domain.Metadata
import com.api.nft.domain.MetadataRepository
import com.api.nft.service.dto.MetadataResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class metadataService(
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