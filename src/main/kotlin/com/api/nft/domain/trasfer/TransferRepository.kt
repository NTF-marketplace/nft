package com.api.nft.domain.trasfer

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface TransferRepository: ReactiveCrudRepository<Transfer,Long> {

    fun findByNftIdOrderByBlockTimestampDesc(nftId: Long) : Flux<Transfer>
}