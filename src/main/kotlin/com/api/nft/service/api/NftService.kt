package com.api.nft.service.api

import com.api.nft.controller.dto.NftMetadataResponse
import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import com.api.nft.event.dto.NftCreatedEvent
import com.api.nft.event.dto.NftResponse
import com.api.nft.service.RedisService
import com.api.nft.service.external.dto.NftAttribute
import com.api.nft.service.external.dto.NftData
import com.api.nft.service.external.dto.NftMetadata
import com.api.nft.service.external.moralis.MoralisApiService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
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
    private val moralisApiService: MoralisApiService,
    private val redisService: RedisService,
) {

    fun findAllById(ids: List<Long>): Flux<NftMetadataResponse> {
        return nftRepository.findAllByNftJoinMetadata(ids)
    }

    fun findOrCreateNft(tokenAddress: String,tokenId: String, chainType: ChainType): Mono<NftResponse> {
        return nftRepository.findByTokenAddressAndTokenIdAndChainType(tokenAddress,tokenId,chainType)
            .switchIfEmpty(
                moralisApiService.getNFTMetadata(tokenAddress,tokenId,chainType)
                    .flatMap { createNftProcess(it,chainType) }
            ).map { it.toResponse() }

    }

    fun getByWalletNft(wallet: String,chainType: ChainType): Flux<NftResponse> {
        return moralisApiService.getNFTsByAddress(wallet, chainType)
            .flatMapMany { Flux.fromIterable(it.result) }
            .filter { it.contractType == "ERC721"}
            .flatMap { findOrCreateNft(it.tokenAddress, it.tokenId, chainType) }
    }


    fun createNftProcess(request: NftData, chainType: ChainType): Mono<Nft> {
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
                        .doOnSuccess { eventPublisher.publishEvent(NftCreatedEvent(this, nft.toResponse())) }
                        .flatMap { createdNft ->
                            redisService.saveNftToRedis(createdNft)
                                .thenReturn(createdNft)
                        }
                        .flatMap {
                            transferService.createTransfer(nft).thenReturn(nft)
                        }
                }
        }
    }

    private fun Nft.toResponse() = NftResponse(
        id =  this.id!!,
        tokenId = this.tokenId,
        tokenAddress = this.tokenAddress,
        chainType = this.chainType,
    )

    fun getNftData(request: NftData, chainType: ChainType): Mono<Triple<NftData, NftMetadata, List<NftAttribute>?>> {
        return Mono.fromCallable {
            val metadata = NftMetadata.toMetadataResponse(request.metadata)
            val attributes = metadata.attributes.let {
                if (it.isNotEmpty()) NftAttribute.toAttributeResponse(it) else null
            }
            Triple(request, metadata, attributes)
        }
    }


    fun createNft(nft: NftData,
                  metadata: NftMetadata?,
                  chainType: ChainType
    ): Mono<Nft> {
        return collectionService.findOrCreate(
            nft.name,
            nft.collectionLogo ?: metadata?.image,
            nft.collectionBannerImage ?: metadata?.image,
            metadata?.description,
            ).flatMap {
            nftRepository.save(
                Nft(
                tokenId = nft.tokenId,
                tokenAddress = nft.tokenAddress,
                chainType = chainType,
                nftName = metadata?.name,
                collectionName = it.name,
                tokenHash = nft.tokenHash,
                amount = nft.amount?.toInt() ?: 0,
                contractType = nft.contractType.toContractEnum(),
                )
            )
        }
    }

    fun String.toContractEnum() : ContractType {
        return when(this) {
            "ERC721" -> ContractType.ERC721
            "ERC1155" -> ContractType.ERC1155
            else -> throw IllegalArgumentException("not support contractType")
        }
    }
}