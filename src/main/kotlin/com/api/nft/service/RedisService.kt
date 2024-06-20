package com.api.nft.service

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

    fun updateToRedis(nftId: Long): Mono<Void> {
        return nftRepository.findByNftJoinMetadata(nftId)
            .flatMap { data ->
                println("data: " + data.id)
                reactiveRedisTemplate.opsForValue().set("NFT:${nftId}", data).then()
            }
    }
}