package com.api.nft.service

import com.api.nft.domain.Metadata
import com.api.nft.domain.MetadataRepository
import com.api.nft.domain.Nft
import com.api.nft.domain.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.service.dto.AttributeResponse
import com.api.nft.service.dto.MetadataResponse
import com.api.nft.service.dto.NftResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class NftService(
    private val moralisApiService: MoralisApiService,
    private val nftRepository: NftRepository,
    private val collectionService: CollectionService,
    private val metadataService: metadataService,
) {

//    fun findOrCreateNft(tokenId: String, tokenAddress: String, chainType: ChainType) {
//        nftRepository.findByTokenAddressAndTokenId(tokenAddress,tokenId)
//            .switchIfEmpty(
//                createNftProcess()
////               getNftByMoralis(tokenId,tokenAddress,chainType)
////                // 이 후 nft외래키를 가지고 metadata 생성
////                // 또 그 이후 nft외래키를 가지고 attribute 생성
//            )
//    }
//
    fun createNftProcess(tokenId: String,
                       tokenAddress: String,
                       chainType: ChainType
    ): Mono<Metadata> {
        val response = getNftByMoralis(tokenId, tokenAddress, chainType)
        return response.flatMap {
            createNft(it.first,it.second,it.third,chainType) // 생성되고 밑에 flatMap 실행 async
                .flatMap {nft ->
                    metadataService.createMetadata(nft.id!!,it.second)
                }
        }
    }

    fun getNftByMoralis(tokenId: String,
                        tokenAddress: String,
                        chainType: ChainType
    )
    : Mono<Triple<NftResponse, MetadataResponse,List<AttributeResponse>>> {
       return moralisApiService.getNft(tokenAddress,tokenId,chainType)
           .filter { !it.possibleSpam }
           .map {
            val metadata = MetadataResponse.toMetadataResponse(it.metadata)
            val attribute = AttributeResponse.toAttributeResponse(metadata.attributes)
            Triple(it,metadata,attribute)
        }
    }

    fun createNft(nft: NftResponse,
                  metadata: MetadataResponse,
                  attribute: List<AttributeResponse>,
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