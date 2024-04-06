package com.api.nft.service

import com.api.nft.domain.attribute.Attribute
import com.api.nft.domain.attribute.AttributeRepository
import com.api.nft.service.external.dto.AttributeResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class AttributeService(
    private val attributeRepository: AttributeRepository,
) {

    fun createAttribute(nftId: Long, attributes: List<AttributeResponse>): Flux<Attribute> {
        return Flux.fromIterable(attributes).flatMap {
            attributeRepository.save(
                Attribute(
                    nftId = nftId,
                    traitType = it.traitType ?: null,
                    value = it.value ?: null
                )
            )
        }
    }
}