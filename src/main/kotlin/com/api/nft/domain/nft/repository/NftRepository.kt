package com.api.nft.domain.nft.repository

import com.api.nft.domain.nft.Nft
import com.api.nft.enums.ChainType
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface NftRepository : ReactiveCrudRepository<Nft,Long>, NftRepositorySupport {

    fun findByTokenAddressAndTokenIdAndChainType(tokenAddress: String, tokenId: String,chainType: ChainType): Mono<Nft>
}