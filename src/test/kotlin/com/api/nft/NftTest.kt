package com.api.nft

import com.api.nft.domain.collection.repository.CollectionRepository
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.domain.trasfer.Transfer
import com.api.nft.enums.ChainType
import com.api.nft.rabbitMQ.RabbitMQSender
import com.api.nft.service.external.moralis.MoralisApiService
import com.api.nft.service.api.NftService
import com.api.nft.service.api.TransferService
import com.api.nft.service.external.dto.EthLogResponse
import com.api.nft.service.external.infura.InfuraApiService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.test.StepVerifier
import java.math.BigInteger

@SpringBootTest
class NftTest(
    @Autowired private val moralisApiService: MoralisApiService,
    @Autowired private val nftService: NftService,
    @Autowired private val collectionRepository: CollectionRepository,
    @Autowired private val rabbitMQSender: RabbitMQSender,
    @Autowired private val transferService: TransferService,
    @Autowired private val nftRepository: NftRepository,
    @Autowired private val infuraApiService: InfuraApiService,
) {

    @Test
    fun test(){
        val tokenAddress = "0x57a133561c74c242a0b70af9c129126244b4869f"
        val tokenId = "4733"
        val res =moralisApiService.getNftTransfer(tokenAddress,tokenId, ChainType.POLYGON_MAINNET).block()
        println(res)
    }

    @Test
    fun createTransfer() {
        val nft = nftRepository.findById(25).block()
        nft?.let {
            transferService.createTransfer(it).block()
        } ?: error("NFT not found")
    }

    @Test
    fun createTransferTest1() {
        val nftMono = nftRepository.findById(22)

        StepVerifier.create(nftMono)
            .assertNext { nft ->
                transferService.createTransfer(nft)
            }
            .verifyComplete()
    }

    @Test
    fun getEth() {
       val res = infuraApiService.getEthLogs("56498172",ChainType.POLYGON_MAINNET,"0xa3784fe9104fdc0b988769fba7459ece2fb36eea","1").block()
        println(res.toString())
    }


    @Test
    fun findOrUpdateByNftId() {
        //56498172
        transferService.findOrUpdateByNftId(31).blockLast()
    }



}