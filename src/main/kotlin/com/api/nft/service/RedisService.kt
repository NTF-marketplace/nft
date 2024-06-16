package com.api.nft.service

import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.NftListing
import com.api.nft.domain.nft.repository.NftRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RedisService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, Any>,
    private val nftRepository: NftRepository,
) {

    private val logger: Logger = LoggerFactory.getLogger(RedisService::class.java)

    fun saveNftToRedis(nft: Nft): Mono<Void> {
        return nftRepository.findByNftJoinMetadata(nft.id!!)
            .doOnNext { data -> logger.info("Data retrieved: $data") }  // Log data to verify it is fetched
            .flatMap { data ->
                println("asdasdasda")
                reactiveRedisTemplate.opsForValue().set("NFT:${nft.id}", data).then()
            }
            .doOnError { error -> logger.error("Error occurred: ${error.message}", error) }  // Log any errors
    }
    fun updateToRedis(nft: NftListing): Mono<Void> {
        return nftRepository.findByNftJoinMetadata(nft.nftId)
            .flatMap { data ->
                reactiveRedisTemplate.opsForValue().set("NFT:${nft.nftId}", data).then()
            }
    }
}