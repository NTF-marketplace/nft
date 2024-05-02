package com.api.nft.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "apikey")
data class ApiKeysProperties(
    val infura: String,
    val moralis: String,
)
