package com.api.nft.controller

import com.api.nft.controller.dto.NftBatchRequest
import com.api.nft.controller.dto.NftResponse
import com.api.nft.domain.nft.repository.NftMetadataDto
import com.api.nft.service.NftService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/nft")
class NftController(
    private val nftService: NftService,
) {

    @PostMapping("/batch")
    fun bath(@RequestBody requests: List<NftBatchRequest>): Flux<NftMetadataDto> {
        return nftService.getBatchNftList(requests)

    }
}