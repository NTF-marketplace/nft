package com.api.nft.service.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class NftResponse(
    val amount: String,
    @JsonProperty("token_id")val tokenId: String,
    @JsonProperty("token_address") val tokenAddress: String,
    @JsonProperty("contract_type") val contractType: String?,
    @JsonProperty("owner_of") val ownerOf: String?,
    @JsonProperty("last_metadata_sync") val lastMetadataSync: String,
    @JsonProperty("last_token_uri_sync") val lastTokenUriSync: String,
    val metadata: String,
    @JsonProperty("block_number") val blockNumber: String,
    @JsonProperty("block_number_minted") val blockNumberMinted: String?,
    val name: String,
    val symbol: String,
    @JsonProperty("token_hash") val tokenHash: String,
    @JsonProperty("token_uri") val tokenUri: String,
    @JsonProperty("minter_address") val minterAddress: String?,
    @JsonProperty("verified_collection") val verifiedCollection: Boolean,
    @JsonProperty("possible_spam") val possibleSpam: Boolean,
    @JsonProperty("collection_logo") val collectionLogo: String?,
    @JsonProperty("collection_banner_image") val collectionBannerImage: String?,
)


data class MetadataResponse(
    val name: String,
    val description: String,
    val image: String,
    @JsonProperty("animation_url") val animationUrl: String?,
    val attributes: List<Map<String, String>>,
    @JsonIgnoreProperties(ignoreUnknown = true) @JsonProperty("external_url") val externalUrl: String? = null
){
    companion object {
        fun toMetadataResponse(metadata: String): MetadataResponse {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(metadata, MetadataResponse::class.java)
        }

        fun parseImage(image: String) : String {
            return image.replace("ipfs://", "https://ipfs.io/ipfs/")
        }
    }
}

data class AttributeResponse(
    @JsonProperty("trait_type") val traitType: String?,
    val value: String?
) {
    companion object {
        fun toAttributeResponse(attributes: List<Map<String, String>>): List<AttributeResponse> {
            return attributes.map {
                AttributeResponse(
                    traitType = it["trait_type"],
                    value = it["value"]
                )
            }
        }
    }
}


