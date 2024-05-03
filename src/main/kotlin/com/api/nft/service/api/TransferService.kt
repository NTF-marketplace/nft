package com.api.nft.service.api

import com.api.nft.domain.nft.Nft
import com.api.nft.domain.trasfer.Transfer
import com.api.nft.domain.trasfer.TransferRepository
import com.api.nft.enums.ChainType
import com.api.nft.properties.ApiKeysProperties
import com.api.nft.service.TransferResponse
import com.api.nft.service.external.dto.NftTransferData
import com.api.nft.service.external.moralis.MoralisApiService
import com.api.nft.util.Util.convertNetworkTypeToChainType
import org.springframework.stereotype.Service
import org.web3j.abi.EventEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Event
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.core.methods.response.Log
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.math.BigInteger


@Service
class TransferService(
    private val moralisApiService: MoralisApiService,
    private val transferRepository: TransferRepository,
) {


     fun createTransfer(nft: Nft)
     {
         moralisApiService.getNftTransfer(
             nft.tokenAddress,
             nft.tokenId,
             nft.chinType.convertNetworkTypeToChainType()
         ).map {
          // transferRepository.save(it.toEntity(nft.id!!))
       }
     }

//    private fun NftTransferData.toEntity(nftId: Long) =
//        Transfer(
//            nftId = nftId,
//            fromAddress = this.result,
//            toAddress = this.to
//        )

}

