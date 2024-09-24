package com.api.nft.controller.dto

import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import io.r2dbc.spi.Row
import java.math.BigDecimal


data class NftMetadataResponse(
    val id: Long,
    val tokenId: String,
    val tokenAddress: String,
    val contractType: ContractType,
    val chainType: ChainType,
    val nftName: String,
    val collectionName: String,
    val image: String,
    val lastPrice: Double?,
    val collectionLogo: String?
){
    companion object {
        fun fromRow(row: Row): NftMetadataResponse {
            val idValue = row.get("id")
            println("ID value type: ${idValue?.javaClass}, value: $idValue")
            return NftMetadataResponse(
                id = row.get("id", Integer::class.java)!!.toLong(),
                tokenId = row.get("tokenid", String::class.java)!!,
                tokenAddress = row.get("tokenaddress", String::class.java)!!,
                chainType = ChainType.valueOf(row.get("chaintype", String::class.java)!!),
                nftName = row.get("nftname", String::class.java)!!,
                collectionName = row.get("collectionname", String::class.java)!!,
                image = row.get("image", String::class.java) ?: "",
                contractType = ContractType.valueOf(row.get("contracttype", String::class.java)!!),
                lastPrice = row.get("lastprice", BigDecimal::class.java)?.toDouble(),
                collectionLogo = row.get("collectionlogo", String::class.java) ?: ""
            )
        }
    }
}
