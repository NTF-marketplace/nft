package com.api.nft.domain.nft.repository

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Mono

class NftRepositorySupportImpl(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
): NftRepositorySupport {
    override fun findByNftJoinMetadata(nftId: Long): Mono<NftMetadataDto> {
        val query =
            """
           SELECT 
                n.id,
                n.token_id AS tokenId, 
                n.token_address AS tokenAddress, 
                n.chain_type AS chinType, 
                n.nft_name AS nftName, 
                n.collection_name AS collectionName, 
                m.image AS image
            FROM 
                nft n
            JOIN 
                metadata m ON n.id = m.nft_id
            WHERE 
                n.id = :$1

            """
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .bind(0, nftId)
            .map { row, data ->
                NftMetadataDto(
                    id = (row.get("id") as Number).toLong(),
                    tokenId = row.get("tokenId", String::class.java)!!,
                    tokenAddress = row.get("tokenAddress", String::class.java)!!,
                    chinType = row.get("chinType", String::class.java)!!,
                    nftName = row.get("nftName", String::class.java)!!,
                    collectionName = row.get("collectionName", String::class.java)!!,
                    image = row.get("image", String::class.java) ?: ""
                )
            }.first()
    }
}