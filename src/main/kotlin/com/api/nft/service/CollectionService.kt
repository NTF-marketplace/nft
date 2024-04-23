package com.api.nft.service

import com.api.nft.domain.collection.Collection
import com.api.nft.domain.collection.repository.CollectionRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CollectionService(
    private val collectionRepository: CollectionRepository,
) {

    fun findOrCreate(name: String) : Mono<Collection> {
        return collectionRepository.findByName(name)
            .switchIfEmpty(
                Mono.defer {
                    collectionRepository.insert(Collection(name = name))
                        .onErrorResume(DuplicateKeyException::class.java){
                            collectionRepository.findByName(name)
                        }

                }

            )
    }
}