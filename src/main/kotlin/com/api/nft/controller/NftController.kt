package com.api.nft.controller

import com.api.nft.domain.nft.repository.NftMetadataDto
import com.api.nft.enums.ChainType
import com.api.nft.service.NftService
import com.api.nft.service.external.dto.NftData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

//TODO("responseEntity로 반환)
@RestController
@RequestMapping("/v1/nft")
class NftController(
    private val nftService: NftService,
) {

    @PostMapping("/save/{chainType}")
    fun save(@PathVariable chainType: ChainType,@RequestBody requests: List<NftData>): Flux<NftMetadataDto> {
        println("들어오는건 맞나요?")
        return nftService.saveNfts(requests,chainType)

    }

    @GetMapping
    fun getAllByIds(@RequestParam nftIds: List<Long>): Flux<NftMetadataDto> {
        return nftService.findAllById(nftIds)
    }
}