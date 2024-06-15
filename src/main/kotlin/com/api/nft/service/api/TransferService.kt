package com.api.nft.service.api

import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.domain.trasfer.Transfer
import com.api.nft.domain.trasfer.TransferRepository
import com.api.nft.enums.ChainType
import com.api.nft.service.external.dto.EthLogResponse
import com.api.nft.service.external.dto.ResultData
import com.api.nft.service.external.infura.InfuraApiService
import com.api.nft.service.external.moralis.MoralisApiService
import com.api.nft.util.Util.convertNetworkTypeToChainType
import com.api.nft.util.Util.toEpochMilli
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigInteger


@Service
class TransferService(
    private val moralisApiService: MoralisApiService,
    private val transferRepository: TransferRepository,
    private val nftRepository: NftRepository,
    private val infuraApiService: InfuraApiService,
) {

    fun createTransfer(nft: Nft): Mono<Void> {
        return moralisApiService.getNftTransfer(
            nft.tokenAddress,
            nft.tokenId,
            nft.chainType
        ).flatMapMany {
            Flux.fromIterable(it.result)
        }.map {
            it.toEntity(nft.id!!)
        }.flatMap { transfer ->
            transferRepository.save(transfer)
        }.doOnError { error ->
            println("Error during transfer creation: $error")
        }.then()
    }
    private fun ResultData.toEntity(nftId: Long): Transfer =
        Transfer(
            nftId = nftId,
            fromAddress = this.fromAddress,
            toAddress = this.toAddress,
            blockNumber = this.blockNumber.toLong(),
            blockTimestamp = this.blockTimestamp.toEpochMilli()
        )

    fun findOrUpdateByNftId(nftId: Long): Flux<Transfer> {
        return nftRepository.findById(nftId).flatMapMany { nft ->
            transferRepository.findByNftIdOrderByBlockTimestampDesc(nftId).next()
                .flatMapMany {
                    updateByTransfer(it,nft)
                        .thenMany( transferRepository.findByNftIdOrderByBlockTimestampDesc(nftId))
            }
        }
    }

    fun updateByTransfer(transfer: Transfer, nft: Nft): Mono<Void> {
        val fromBlock = transfer.blockNumber + 1
        return infuraApiService.getEthLogs(
            fromBlock.toString(),
            nft.chainType,
            nft.tokenAddress,
            nft.tokenId
        ).flatMapMany { ethLogResponses ->
            Flux.fromIterable(ethLogResponses)
        }.flatMap { ethLogResponse ->
            ethLogResponse.toTransferEntity(nft.id!!, nft.chainType)
                .flatMap {
                    transferRepository.save(it)
                }
        }.then()
    }


    private fun EthLogResponse.toTransferEntity(nftId: Long, chainType: ChainType): Mono<Transfer> {
        return infuraApiService.getBlockTimestamp(blockNumber, chainType)
            .map { timestamp ->
                Transfer(
                    toAddress = parseAddress(topics[2]),
                    fromAddress = parseAddress(topics[1]), blockNumber = BigInteger(blockNumber.substring(2), 16).toLong(),
                    nftId = nftId,
                    blockTimestamp = timestamp
                )
            }
    }

    private fun parseAddress(address: String): String {
        return "0x" + address.substring(26).padStart(40, '0')
    }

}

