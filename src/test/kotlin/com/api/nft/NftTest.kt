package com.api.nft

import com.api.nft.domain.collection.repository.CollectionRepository
import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.repository.NftListingRepository
import com.api.nft.domain.nft.repository.NftRepository
import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import com.api.nft.enums.TokenType
import com.api.nft.event.dto.NftCreatedEvent
import com.api.nft.rabbitMQ.RabbitMQSender
import com.api.nft.service.external.moralis.MoralisApiService
import com.api.nft.service.api.NftService
import com.api.nft.service.api.TransferService
import com.api.nft.event.dto.NftResponse
import com.api.nft.service.RedisService
import com.api.nft.service.api.NftListingService
import com.api.nft.service.dto.ListingResponse
import com.api.nft.service.external.binance.BinanceApiService
import com.api.nft.service.external.infura.InfuraApiService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import reactor.test.StepVerifier
import java.math.BigDecimal

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
    @Autowired private val binanceApiService: BinanceApiService,
    @Autowired private val redisService: RedisService,
    @Autowired private val nftListingService: NftListingService,
    @Autowired private val nftListingRepository: NftListingRepository,
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
            chainType = ChainType.POLYGON_MAINNET,
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
        chainType = this.chainType,
    )


    @Test
    fun getByWalletNft() {
        val wallet = "0x01b72b4aa3f66f213d62d53e829bc172a6a72867"
        val res= nftService.getByWalletNft(wallet,ChainType.POLYGON_MAINNET).blockLast()
        println(res?.tokenId)
        println(res?.tokenAddress)

    }

    @Test
    fun binanceTest() {
        val res = binanceApiService.getTickerPrice(TokenType.ETH).block()
        println(res?.symbol)
        println(res?.price)
    }

    @Test
    fun transferTest() {
        val nft = nftRepository.findById(3L).block()

        transferService.createTransfer(nft!!).block()

    }

    @Test
    fun trasfermoralist() {
        val res =moralisApiService.getNftTransfer("0xa3784fe9104fdc0b988769fba7459ece2fb36eea","0",ChainType.POLYGON_MAINNET).block()
        println(res.toString())
    }

    @Test
    fun testNftData() {
        val res =nftRepository.findByNftJoinMetadata(3).block()
        println(res.toString())
    }


//    @Test
//    fun nftListing() {
//         val listing = ListingResponse(
//            id = 1,
//            nftId = 3L,
//            address = "0x01b72b4aa3f66f213d62d53e829bc172a6a72867",
//            createdDateTime = 1714662809000,
//            endDateTime = 1714662809000,
//            price = BigDecimal(0.23),
//            tokenType = TokenType.ETH
//
//        )
//        nftListingService.update(listing).block()
//    }

    @Test
    fun redisTest22( ){
        // val nftlisting = nftListingRepository.findByNftId(1L).block()
        redisService.updateToRedis(1L).block()
    }

    @Test
    fun redisTest() {
        val nft = nftRepository.findById(8).block()
        redisService.updateToRedis(nft?.id!!).block()
        // val res = nftRepository.findByNftJoinMetadata(1).block()


    }

    @Test
    fun nftService() {
        val nft = nftService.findOrCreateNft(tokenAddress = "0x524cAB2ec69124574082676e6F654a18df49A048", tokenId = "5430",ChainType.ETHEREUM_MAINNET).block()

    }

    @Test
    fun asdasd() {
        nftService.getByWalletNft("0x01b72b4aa3f66f213d62d53e829bc172a6a72867",ChainType.POLYGON_MAINNET).blockLast()
        // Thread.sleep(10000)

    }

    @Test
    fun test() {
        val address = "0x01b72b4aa3f66f213d62d53e829bc172a6a72867"

        val res= moralisApiService.getNFTsByAddress(address,ChainType.POLYGON_MAINNET).block()
        println(res.toString())
    }

}