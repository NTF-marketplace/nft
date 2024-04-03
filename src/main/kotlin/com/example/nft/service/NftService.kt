package com.example.nft.service

import com.example.nft.enums.ChainType
import org.springframework.stereotype.Service

@Service
class NftService(
    private val moralisApiService: MoralisApiService
) {

    fun findOrSave(tokenId: String, tokenAddress:String, chainType: ChainType) {

    }
}