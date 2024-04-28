package com.api.nft.service.api

import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftMetadataDto
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.event.NftCreatedEvent
import com.api.nft.service.external.dto.AttributeData
import com.api.nft.service.external.dto.MetadataData
import com.api.nft.service.external.dto.NftData
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class NftService(
    private val nftRepository: NftRepository,
    private val collectionService: CollectionService,
    private val metadataService: MetadataService,
    private val attributeService: AttributeService,
    private val eventPublisher: ApplicationEventPublisher,
    private val transferService: TransferService,
) {

    @Transactional
    fun saveNfts(requests: List<NftData>, chainType: ChainType): Flux<NftMetadataDto> {
        return Flux.fromIterable(requests)
            .flatMap { findOrCreateNft(it, chainType) }
    }

    fun findAllById(ids: List<Long>): Flux<NftMetadataDto> {
        return nftRepository.findAllByNftJoinMetadata(ids)
    }

    @Transactional
    fun findOrCreateNft(request:NftData, chainType: ChainType): Mono<NftMetadataDto> {
       return nftRepository.findByTokenAddressAndTokenId(request.tokenAddress,request.tokenId)
            .switchIfEmpty(
                createNftProcess(request,chainType)
            ).flatMap {
                nftRepository.findByNftJoinMetadata(it.id!!)
            }
    }

    fun createNftProcess(request: NftData,
                         chainType: ChainType
    ): Mono<Nft> {
        val response = getNftData(request, chainType)
        return response.flatMap { (nftData, metadataData, attributeDataList) ->
            createNft(nftData, metadataData, chainType)
                .flatMap { nft ->
                    metadataService.createMetadata(nft.id!!, metadataData)
                        .thenMany(attributeService.createAttribute(
                            nft.id,
                            attributeDataList ?: emptyList()
                        ))
                        .then(Mono.just(nft))
                        .doOnSuccess { eventPublisher.publishEvent(NftCreatedEvent(this, nft)) }

                }
        }
    }


    fun getNftData(request: NftData, chainType: ChainType): Mono<Triple<NftData, MetadataData, List<AttributeData>?>> {
        return Mono.fromCallable {
            val metadata = MetadataData.toMetadataResponse(request.metadata)
            val attributes = metadata?.attributes?.let {
                if (it.isNotEmpty()) AttributeData.toAttributeResponse(it) else null
            }
            Triple(request, metadata, attributes)
        }
    }


    fun createNft(nft: NftData,
                  metadata: MetadataData,
                  chainType: ChainType
    ): Mono<Nft> {
        return collectionService.findOrCreate(nft.name,nft.collectionLogo,nft.collectionBannerImage).flatMap {
            nftRepository.save(
                Nft(
                tokenId = nft.tokenId,
                tokenAddress = nft.tokenAddress,
                chinType = chainType.toString(),
                nftName = metadata.name,
                collectionName = it.name,
                tokenHash = nft.tokenHash,
                amount = nft.amount.toInt(),
                contractType = nft.contractType!!,
                )
            )
        }
    }
}