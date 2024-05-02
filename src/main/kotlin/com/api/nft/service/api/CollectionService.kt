package com.api.nft.service.api

import com.api.nft.domain.collection.Collection
import com.api.nft.domain.collection.repository.CollectionRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CollectionService(
    private val collectionRepository: CollectionRepository,
) {

    fun findOrCreate(
        name: String,
        logo: String?,
        bannerImage: String?,
        description: String?,
    ) : Mono<Collection> {
        return collectionRepository.findByName(name)
            .switchIfEmpty(
                Mono.defer {
                    collectionRepository.insert(
                        Collection(
                            name = name,
                            logo = logo,
                            bannerImage = bannerImage,
                            description = description,
                            ))
                        .onErrorResume(DuplicateKeyException::class.java){
                            collectionRepository.findByName(name)
                        }

                }

            )
    }
}