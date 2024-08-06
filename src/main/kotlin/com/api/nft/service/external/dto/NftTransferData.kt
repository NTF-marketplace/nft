package com.api.nft.service.external.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class NftTransferData(
    val page: String,
    @JsonProperty("page_size")val pageSize: String?,
    val cursor: String?,
    val result: List<ResultData>,
    @JsonProperty("block_exists") val blockExists: Boolean,
    @JsonIgnoreProperties(ignoreUnknown = true) @JsonProperty("index_complete") val indexComplete: Boolean
)

data class  ResultData (
    @JsonProperty("token_address") val tokenAddress: String,
    @JsonProperty("token_id") val tokenId: String,
    @JsonProperty("from_address") val fromAddress: String,
    @JsonProperty("from_address_label")val fromAddressLabel: String?,
    @JsonProperty("to_address") val toAddress: String,
    @JsonProperty("to_address_label") val toAddressLabel: String?,
    val value: String,
    val amount: String,
    @JsonProperty("contract_type") val contractType: String,
    @JsonProperty("block_number") val blockNumber: String,
    @JsonProperty("block_timestamp") val blockTimestamp: String,
    @JsonProperty("block_hash") val blockHash: String,
    @JsonProperty("transaction_hash") val transactionHash: String,
    @JsonProperty("transaction_type") val transactionType: String,
    @JsonProperty("transaction_index") val transactionIndex: String,
    @JsonProperty("log_index") val logIndex: String,
    val operator: String?,
    @JsonProperty("possible_spam") val possibleSpam: Boolean,
    @JsonProperty("verified_collection") val verifiedCollection : String,
    @JsonProperty("verified") val verified : Int,
)
