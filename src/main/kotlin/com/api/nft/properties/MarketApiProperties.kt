package com.api.nft.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "market")
data class MarketApiProperties(
    val uri: String
)
