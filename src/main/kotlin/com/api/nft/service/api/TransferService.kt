package com.api.nft.service.api

import com.api.nft.domain.nft.Nft
import com.api.nft.domain.trasfer.Transfer
import com.api.nft.domain.trasfer.TransferRepository
import com.api.nft.service.external.dto.ResultData
import com.api.nft.service.external.moralis.MoralisApiService
import com.api.nft.util.Util.convertNetworkTypeToChainType
import com.api.nft.util.Util.toEpochMilli
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux


@Service
class TransferService(
    private val moralisApiService: MoralisApiService,
    private val transferRepository: TransferRepository,
) {

    fun createTransfer(nft: Nft) {
        moralisApiService.getNftTransfer(
            nft.tokenAddress,
            nft.tokenId,
            nft.chinType.convertNetworkTypeToChainType()
        ).flatMapMany {
            Flux.fromIterable(it.result)
        }.map {
            it.toEntity(nft.id!!)
        }.flatMap { transfer ->
            transferRepository.save(transfer)
        }.doOnError { error ->
            println("Error during transfer creation: $error")
        }.then()
            .subscribe()
    }


    private fun ResultData.toEntity(nftId: Long): Transfer =
        Transfer(
            nftId = nftId,
            fromAddress = this.fromAddress,
            toAddress = this.toAddress,
            blockNumber = this.blockNumber.toLong(),
            blockTimestamp = this.blockTimestamp.toEpochMilli()
        )

}

