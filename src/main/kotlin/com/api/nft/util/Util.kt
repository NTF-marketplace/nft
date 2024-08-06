package com.api.nft.util

import com.api.nft.enums.ChainType
import java.time.Instant
import java.time.format.DateTimeFormatter

object Util {
    fun String.convertNetworkTypeToChainType(): ChainType {
        return when (this) {
            "ETHEREUM_MAINNET" -> ChainType.ETHEREUM_MAINNET
            "POLYGON_MAINNET" -> ChainType.POLYGON_MAINNET
            else -> throw IllegalArgumentException("Unknown network type: $this")
        }
    }

    fun timestampToString(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        return DateTimeFormatter.ISO_INSTANT.format(instant)
    }

    fun Long.toIsoString(): String = DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(this))

    fun String.toEpochMilli(): Long {
        return Instant.parse(this).toEpochMilli()
    }

}