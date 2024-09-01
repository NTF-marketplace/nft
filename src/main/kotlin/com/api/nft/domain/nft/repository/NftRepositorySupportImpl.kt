package com.api.nft.domain.nft.repository

import com.api.nft.controller.dto.NftMetadataResponse
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class NftRepositorySupportImpl(
    private val r2dbcEntityTemplate: R2dbcEntityTemplate
): NftRepositorySupport {
    override fun findByNftJoinMetadata(id: Long): Mono<NftMetadataResponse> {
        val query = """
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
            c.logo AS collectionLogo
        FROM 
            nft n
        LEFT JOIN 
            collection c ON n.collection_name = c.name    
        LEFT JOIN 
            metadata m ON n.id = m.nft_id
        LEFT JOIN 
            nft_listing nl ON n.id = nl.nft_id    
        WHERE 
            n.id = :$1
    """
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .bind(0, id)
            .map { row, _ -> NftMetadataResponse.fromRow(row) }
            .first()
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
        FROM 
            nft n
        LEFT JOIN 
            collection c ON n.collection_name = c.name    
        LEFT JOIN 
            metadata m ON n.id = m.nft_id
        LEFT JOIN 
            nft_listing nl ON n.id = nl.nft_id    
        WHERE 
            n.id IN (:$1)
    """
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .bind(0, ids)
            .map { row, _ -> NftMetadataResponse.fromRow(row) }
            .all()
    }

}