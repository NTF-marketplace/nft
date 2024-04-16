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

@RestController
@RequestMapping("/v1/nft")
class NftController(
    private val nftService: NftService,
) {

    // TODO(getNftResponse 함수명 변겅)
    @PostMapping("/batch")
    fun batch(@RequestBody requests: List<NftBatchRequest>): Flux<NftMetadataDto> {
        println("emfdjdha??sdasd")
        return nftService.getBatchNftList(requests)
    }
}