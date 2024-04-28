package com.api.nft.service.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

data class NftData(
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


data class MetadataData(
    val name: String,
    val description: String,
    val image: String,
    @JsonProperty("animation_url") val animationUrl: String?,
    val attributes: List<Map<String, String>> = emptyList(),
    @JsonIgnoreProperties(ignoreUnknown = true) @JsonProperty("external_url") val externalUrl: String? = null,
    @JsonIgnoreProperties(ignoreUnknown = true) @JsonProperty("youtube_url") val youtubeUrl: String? = null,
){
    companion object {
        fun toMetadataResponse(metadata: String): MetadataData {
            val mapper = jacksonObjectMapper()
            return mapper.readValue(metadata, MetadataData::class.java)
        }

        fun parseImage(image: String) : String {
            return image.replace("ipfs://", "https://ipfs.io/ipfs/")
        }
    }
}

data class AttributeData(
    @JsonProperty("trait_type") val traitType: String?,
    val value: String?
) {
    companion object {
        fun toAttributeResponse(attributes: List<Map<String, String>>): List<AttributeData> {
            return attributes.map {
                AttributeData(
                    traitType = it["trait_type"],
                    value = it["value"]
                )
            }
        }
    }
}


