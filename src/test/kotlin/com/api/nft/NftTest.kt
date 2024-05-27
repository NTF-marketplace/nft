package com.api.nft

import com.api.nft.domain.collection.repository.CollectionRepository
import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import com.api.nft.event.dto.NftCreatedEvent
import com.api.nft.rabbitMQ.RabbitMQSender
import com.api.nft.service.external.moralis.MoralisApiService
import com.api.nft.service.api.NftService
import com.api.nft.service.api.TransferService
import com.api.nft.event.dto.NftResponse
import com.api.nft.service.external.infura.InfuraApiService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import reactor.test.StepVerifier

@SpringBootTest
class NftTest(
    @Autowired private val moralisApiService: MoralisApiService,
    @Autowired private val nftService: NftService,
    @Autowired private val collectionRepository: CollectionRepository,
    @Autowired private val rabbitMQSender: RabbitMQSender,
    @Autowired private val transferService: TransferService,
    @Autowired private val nftRepository: NftRepository,
    @Autowired private val infuraApiService: InfuraApiService,
    @Autowired private val eventPublisher: ApplicationEventPublisher,
) {


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

    @Test
    fun rabbitMQTest() {
       val nft = Nft(
            id = 1,
            tokenId = "hello",
            tokenAddress = "helloAddress",
            chinType = ChainType.POLYGON_MAINNET,
            contractType = ContractType.ERC721,
            nftName = "nftName",
            tokenHash = null,
            collectionName = "nftCollection",
            amount = 3
        )
        eventPublisher.publishEvent(NftCreatedEvent(this,nft.toResponse()))
        // rabbitMQSender.nftSend(nft.toResponse())
         // rabbitMQSender.nftSend1("asdasasdas")
    }

    private fun Nft.toResponse() = NftResponse(
        id =  this.id!!,
        tokenId = this.tokenId,
        tokenAddress = this.tokenAddress,
        chainType = this.chinType,
        nftName = this.nftName,
        collectionName = this.collectionName
    )






}