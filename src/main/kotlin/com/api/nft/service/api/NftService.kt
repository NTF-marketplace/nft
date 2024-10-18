package com.api.nft.service.api

import com.api.nft.controller.dto.NftMetadataResponse
import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import com.api.nft.event.dto.NftCreatedEvent
import com.api.nft.event.dto.NftResponse
import com.api.nft.service.RedisService
import com.api.nft.service.dto.NftDetailResponse
import com.api.nft.service.external.MarketApiService
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
    private val marketApiService: MarketApiService,
    private val nftListingService: NftListingService,
    private val nftAuctionService: NftAuctionService,
) {

    fun findByNftDetail1(nftId: Long): Mono<NftDetailResponse> {
        return nftRepository.findById(nftId)
            .flatMap { nft ->
                val metadataMono = metadataService.findByMetadata(nftId)
                val attributeMono = attributeService.findByAttribute(nftId).collectList()
                val transferMono = transferService.findOrUpdateByNftId(nftId).collectList()
                val listingMono = nftListingService.findByNftId(nftId)
                val auctionMono = nftAuctionService.findByNftId(nftId)
                    .flatMap { auction ->
                        marketApiService.getOfferHistory(auction.id).collectList()
                            .map { offers -> auction to offers }
                    }.defaultIfEmpty(null to emptyList())

                val ledgerMono = marketApiService.getLedgerHistory(nftId).collectList()

                Mono.zip(metadataMono, attributeMono, transferMono, listingMono, auctionMono, ledgerMono)
                    .map { tuple ->
                        val metadata = tuple.t1
                        val attributes = tuple.t2
                        val transfers = tuple.t3
                        val listing = tuple.t4
                        val (auction, offers) = tuple.t5
                        val ledgers = tuple.t6

                        NftDetailResponse(
                            nft = nft,
                            metadata = metadata,
                            attributes = attributes,
                            transfers = transfers,
                            listing = listing,
                            auction = auction,
                            offers = offers,
                            ledgers = ledgers,
                        )
                    }
            }
    }


    fun findAllById(ids: List<Long>): Flux<NftMetadataResponse> {
        return nftRepository.findAllByNftJoinMetadata(ids)
            .flatMap { nft ->
                redisService.updateToRedis(nft.id)
                    .thenReturn(nft)
            }
    }

    fun findById(id: Long): Mono<NftMetadataResponse> {
        return nftRepository.findByNftJoinMetadata(id)
            .flatMap { nft ->
                redisService.updateToRedis(nft.id)
                    .thenReturn(nft)
            }
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
        return response
            .flatMap { (nftData, metadataData, attributeDataList) ->
                createMetadata(nftData, metadataData, attributeDataList ,chainType) }
            .flatMap { createdNft ->
                redisService.updateToRedis(createdNft.id!!).thenReturn(createdNft) }
            // .flatMap { nft ->
            //     transferService.createTransfer(nft).thenReturn(nft) }
            .doOnSuccess {
                eventPublisher.publishEvent(NftCreatedEvent(this, it.toResponse()))
            }
    }


    private fun createMetadata(
        nftData: NftData,
        metadataData: NftMetadata,
        attributeDataList: List<NftAttribute>?,
        chainType: ChainType
    ): Mono<Nft> {
        return createNft(nftData, metadataData, chainType)
            .flatMap { nft ->
                metadataService.createMetadata(nft.id!!, metadataData)
                    .thenMany(attributeService.createAttribute(nft.id, attributeDataList ?: emptyList()))
                    .then(Mono.just(nft))
            }
    }

    private fun Nft.toResponse() = NftResponse(
        id =  this.id!!,
        tokenId = this.tokenId,
        tokenAddress = this.tokenAddress,
        chainType = this.chainType,
        collectionName = this.collectionName
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