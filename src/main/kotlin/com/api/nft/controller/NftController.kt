package com.api.nft.controller

import com.api.nft.domain.nft.repository.NftMetadataDto
import com.api.nft.domain.trasfer.Transfer
import com.api.nft.enums.ChainType
import com.api.nft.event.dto.NftResponse
import com.api.nft.service.api.NftService
import com.api.nft.service.api.TransferService
import com.api.nft.service.external.dto.NftData
import com.api.nft.service.external.dto.NftRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

//TODO("responseEntity로 반환)
@RestController
@RequestMapping("/v1/nft")
class NftController(
    private val nftService: NftService,
    private val transferService: TransferService,
) {

    // @PostMapping("/save/{chainType}")
    // fun save(@PathVariable chainType: ChainType,@RequestBody requests: List<NftData>): Flux<NftMetadataDto> {
    //     return nftService.saveNfts(requests,chainType)
    //
    // }

    @GetMapping
    fun getAllByIds(@RequestParam nftIds: List<Long>): Flux<NftMetadataDto> {
        return nftService.findAllById(nftIds)
    }

    @GetMapping("/transfer")
    fun getTransfers(@RequestParam nftId: Long) : Mono<ResponseEntity<List<Transfer>>> {
        return transferService.findOrUpdateByNftId(nftId)
            .collectList()
            .map { ResponseEntity.ok(it) }
            .defaultIfEmpty( ResponseEntity.notFound().build())
            .onErrorResume { Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()) }
    }

    @PostMapping
    fun findOrCreate(@RequestBody request: NftRequest): Mono<NftResponse> {
        return nftService.findOrCreateNft(request.tokenAddress,request.tokenId,request.chainType)
    }

    @GetMapping("/wallet/{chainType}")
    fun getByWalletNft(@PathVariable chainType: ChainType, @RequestParam wallet: String) : Flux<NftResponse> {
        return nftService.getByWalletNft(wallet,chainType)

    }


}