package com.api.nft.service.api

import com.api.nft.domain.metadata.Metadata
import com.api.nft.domain.metadata.repository.MetadataRepository
import com.api.nft.service.external.dto.NftMetadata
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MetadataService(
    private val metadataRepository: MetadataRepository,
) {

    fun createMetadata(nftId: Long, metadata: NftMetadata): Mono<Metadata> {
        return metadataRepository.save(
            Metadata(
                nftId = nftId,
                description = metadata.description,
                image = NftMetadata.parseImage(metadata.image),
                animationUrl = metadata.animationUrl,
            )
        )

    }
}