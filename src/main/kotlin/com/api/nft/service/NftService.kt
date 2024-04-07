package com.api.nft.service

import com.api.nft.controller.dto.NftBatchRequest
import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftMetadataDto
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.service.external.dto.AttributeResponse
import com.api.nft.service.external.dto.MetadataResponse
import com.api.nft.service.external.dto.NftResponse
import com.api.nft.service.external.moralis.MoralisApiService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class NftService(
    private val moralisApiService: MoralisApiService,
    private val nftRepository: NftRepository,
    private val collectionService: CollectionService,
    private val metadataService: MetadataService,
    private val attributeService: AttributeService,
) {

    fun getBatchNftList(reqeusts : List<NftBatchRequest>): Flux<NftMetadataDto> {
        return Flux.fromIterable(reqeusts)
            .flatMap {
                findOrCreateNft(it.tokenId,it.tokenAddress,it.chainType)
            }
    }

    @Transactional
    fun findOrCreateNft(tokenId: String, tokenAddress: String, chainType: ChainType): Mono<NftMetadataDto> {
       return nftRepository.findByTokenAddressAndTokenId(tokenAddress,tokenId)
            .switchIfEmpty(
                createNftProcess(tokenId,tokenAddress,chainType)
            ).flatMap {
                nftRepository.findByNftJoinMetadata(it.id!!)
            }
    }

    fun createNftProcess(tokenId: String,
                         tokenAddress: String,
                         chainType: ChainType
    ): Mono<Nft> {
        val response = getNftByMoralis(tokenId, tokenAddress, chainType)
        return response.flatMap { (nftResponse, metadataResponse, attributeResponseList) ->
            createNft(nftResponse, metadataResponse, chainType)
                .flatMap { nft ->
                    metadataService.createMetadata(nft.id!!, metadataResponse)
                        .thenMany(attributeService.createAttribute(
                            nft.id,
                            attributeResponseList ?: emptyList()
                        ))
                        .then(Mono.just(nft))
                }
        }
    }

    fun getNftByMoralis(tokenId: String,
                        tokenAddress: String,
                        chainType: ChainType
    )
    : Mono<Triple<NftResponse, MetadataResponse,List<AttributeResponse>?>> {
       return moralisApiService.getNft(tokenAddress,tokenId,chainType)
           .filter { !it.possibleSpam }
           .map {
            val metadata = MetadataResponse.toMetadataResponse(it.metadata)
               val attributes = if (metadata != null && metadata.attributes.isNotEmpty())
                   AttributeResponse.toAttributeResponse(metadata.attributes)
               else null
            Triple(it,metadata,attributes)
        }
    }

    fun createNft(nft: NftResponse,
                  metadata: MetadataResponse,
                  chainType: ChainType
    ): Mono<Nft> {
        return collectionService.findOrCreate(nft.name).flatMap {
            nftRepository.save(
                Nft(
                tokenId = nft.tokenId,
                tokenAddress = nft.tokenAddress,
                chinType = chainType.toString(),
                nftName = metadata.name,
                collectionName = it.name,
                ownerOf = nft.ownerOf,
                tokenHash = nft.tokenHash,
                amount = nft.amount.toInt()
                )
            )
        }
    }
}