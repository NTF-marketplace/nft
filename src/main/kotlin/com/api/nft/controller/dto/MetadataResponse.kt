package com.api.nft.controller.dto

import com.api.nft.domain.metadata.Metadata

data class MetadataResponse(
    val id: Long?,
    val description: String?,
    val image: String?,
    val animationUrl: String?,
){
    companion object{
        fun Metadata.toResponse() = MetadataResponse(
            id = id,
            description = description,
            image = image,
            animationUrl = animationUrl
        )

    }
}
