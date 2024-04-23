package com.api.nft.util

import com.api.nft.enums.ChainType

object Util {
    fun String.convertNetworkTypeToChainType(): ChainType {
        return when (this) {
            "ETHEREUM" -> ChainType.ETHEREUM_MAINNET
            "POLYGON" -> ChainType.POLYGON_MAINNET
            else -> throw IllegalArgumentException("Unknown network type: $this")
        }
    }

}