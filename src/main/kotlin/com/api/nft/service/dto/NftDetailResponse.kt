package com.api.nft.service.dto

import com.api.nft.controller.dto.MetadataResponse
import com.api.nft.controller.dto.OfferResponse
import com.api.nft.controller.dto.OrdersResponse
import com.api.nft.domain.attribute.Attribute
import com.api.nft.domain.metadata.Metadata
import com.api.nft.domain.nft.Nft
import com.api.nft.domain.nft.NftAuction
import com.api.nft.domain.nft.NftListing
import com.api.nft.domain.trasfer.Transfer


data class NftDetailResponse(
    val nft : Nft,
    val metadata : MetadataResponse,
    val attributes : List<Attribute>,
    val transfers : List<Transfer>,
    val listing : NftListing?,
    val auction : NftAuction?,
    val offers: List<OfferResponse>?,
    val ledgers: List<OrdersResponse>?
)
