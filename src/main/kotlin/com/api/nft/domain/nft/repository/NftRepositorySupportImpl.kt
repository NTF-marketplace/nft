package com.api.nft.domain.nft.repository

import com.api.nft.controller.dto.NftMetadataResponse
import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import com.api.nft.enums.TokenType
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

class NftRepositorySupportImpl(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
): NftRepositorySupport {
    override fun findByNftJoinMetadata(id: Long): Mono<NftMetadataResponse> {
        val query =
            """
           SELECT 
                n.id,
                n.token_id AS tokenId, 
                n.token_address AS tokenAddress, 
                n.chain_type AS chainType, 
                n.nft_name AS nftName, 
                n.collection_name AS collectionName,
                n.contract_type AS contractType,
                m.image AS image,
                nl.price AS lastPrice,
                nl.token_type AS tokenType
            FROM 
                nft n
            JOIN 
                metadata m ON n.id = m.nft_id
            JOIN nft_listing nl ON n.id = nl.nft_id    
            WHERE 
                n.id = :$1
            """
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .bind(0, id)
            .map { row, data ->
                NftMetadataResponse(
                    id = (row.get("id") as Number).toLong(),
                    tokenId = row.get("tokenId", String::class.java)!!,
                    tokenAddress = row.get("tokenAddress", String::class.java)!!,
                    chainType = row.get("chainType", ChainType::class.java)!!,
                    nftName = row.get("nftName", String::class.java)!!,
                    collectionName = row.get("collectionName", String::class.java)!!,
                    image = row.get("image", String::class.java) ?: "",
                    contractType = row.get("contractType", ContractType::class.java)!!,
                    lastPrice = row.get("lastPrice", BigDecimal::class.java),
                    tokenType = row.get("tokenType", TokenType::class.java)
                )
            }.first()
    }

    override fun findAllByNftJoinMetadata(ids: List<Long>): Flux<NftMetadataResponse> {
        val query = """
       SELECT 
            n.id,
            n.token_id AS tokenId, 
            n.token_address AS tokenAddress, 
            n.chain_type AS chainType, 
            n.nft_name AS nftName, 
            n.collection_name AS collectionName, 
            n.contract_type AS contractType,
            m.image AS image
            nl.token_type AS tokenType
        FROM 
            nft n
        JOIN 
            metadata m ON n.id = m.nft_id
        JOIN nft_listing nl ON nft.id = nl.nft_id    
        WHERE 
            n.id IN (:$1)
    """
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .bind(0, ids)
            .map { row, metadata ->
                NftMetadataResponse(
                    id = (row.get("id") as Number).toLong(),
                    tokenId = row.get("tokenId", String::class.java)!!,
                    tokenAddress = row.get("tokenAddress", String::class.java)!!,
                    chainType = row.get("chainType", ChainType::class.java)!!,
                    nftName = row.get("nftName", String::class.java)!!,
                    collectionName = row.get("collectionName", String::class.java)!!,
                    image = row.get("image", String::class.java) ?: "",
                    contractType = row.get("contractType", ContractType::class.java)!!,
                    lastPrice = row.get("lastPrice", BigDecimal::class.java),
                    tokenType = row.get("tokenType", TokenType::class.java),
                )
            }
            .all()
    }

}