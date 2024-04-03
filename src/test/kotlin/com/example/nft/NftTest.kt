package com.example.nft

import com.example.nft.enums.ChainType
import com.example.nft.service.MoralisApiService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NftTest(
    @Autowired private val moralisApiService: MoralisApiService,
) {

    @Test
    fun test(){
        val tokenAddress = "0x1f980cfdf257792f2d85523094cd6b7210cab509"
        val tokenId = "2"
        val res =moralisApiService.getNFTsByAddress(tokenAddress,tokenId,ChainType.POLYGON_MAINNET).block()
        println(res)
    }
}