package com.api.nft

import com.api.nft.domain.collection.Collection
import com.api.nft.domain.collection.repository.CollectionRepository
import com.api.nft.enums.ChainType
import com.api.nft.service.external.moralis.MoralisApiService
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
        val tokenAddress = "0x57a133561c74c242a0b70af9c129126244b4869f"
        val tokenId = "4733"
        val res =moralisApiService.getNft(tokenAddress,tokenId, ChainType.POLYGON_MAINNET).block()
        println(res)
    }

//    @Test
//    fun test1() {
//        val tokenAddress = "0x57a133561c74c242a0b70af9c129126244b4869f"
//        val tokenId = "4733"
//        val res =nftService.getNftByMoralis(tokenId,tokenAddress, ChainType.POLYGON_MAINNET).block()
//        println(res?.first.toString())
//        println(res?.second.toString())
//        println(res?.third.toString())
//    }
//
//    @Test
//    fun test2() {
//        val tokenAddress = "0xe7900239e9332060dc975ed6f0cc5f0129d924cf"
//        val tokenId = "3"
//        nftService.createNftProcess(tokenId,tokenAddress, ChainType.POLYGON_MAINNET).block()
//    }

    @Test
    fun test3() {
        val tokenName = "asdasdasd"
        collectionRepository.insert(Collection(tokenName)).block()
    }

//    @Test
//    fun test4() {
//        val tokenAddress = "0xe4a8bfdc0684f62b4cfb43165021814f059819ef"
//        val tokenId = "4617"
//        val res = nftService.findOrCreateNft(tokenId,tokenAddress,ChainType.POLYGON_MAINNET).block()
//        println(res.toString())
//    }
}