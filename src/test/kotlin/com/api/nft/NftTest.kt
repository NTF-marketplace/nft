package com.api.nft

import com.api.nft.domain.Collection
import com.api.nft.domain.CollectionRepository
import com.api.nft.enums.ChainType
import com.api.nft.service.MoralisApiService
import com.api.nft.service.NftService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NftTest(
    @Autowired private val moralisApiService: MoralisApiService,
    @Autowired private val nftService: NftService,
    @Autowired private val collectionRepository: CollectionRepository,
) {

    @Test
    fun test(){
        val tokenAddress = "0xe7900239e9332060dc975ed6f0cc5f0129d924cf"
        val tokenId = "3"
        val res =moralisApiService.getNft(tokenAddress,tokenId, ChainType.POLYGON_MAINNET).block()
        println(res)
    }

    @Test
    fun test1() {
        val tokenAddress = "0xe7900239e9332060dc975ed6f0cc5f0129d924cf"
        val tokenId = "3"
        val res =nftService.getNftByMoralis(tokenId,tokenAddress, ChainType.POLYGON_MAINNET).block()
    }

    @Test
    fun test2() {
        val tokenAddress = "0xe7900239e9332060dc975ed6f0cc5f0129d924cf"
        val tokenId = "3"
        nftService.createNftProcess(tokenId,tokenAddress, ChainType.POLYGON_MAINNET).block()
    }

    @Test
    fun test3() {
        val tokenName = "asdasdasd"
        collectionRepository.insert(Collection(tokenName)).block()
    }
}