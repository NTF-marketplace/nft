package com.api.nft.enums

enum class ChainType{
        ETHEREUM_MAINNET,
        LINEA_MAINNET,
        LINEA_SEPOLIA,
        POLYGON_MAINNET,
        ETHEREUM_HOLESKY,
        ETHEREUM_SEPOLIA,
        POLYGON_AMOY,

}


enum class NetworkType{
        ETHEREUM,
        POLYGON,
}

enum class ContractType{
        ERC721, ERC1155
}

enum class TokenType {
        SAND, MATIC, ETH, BTC
}

enum class StatusType { RESERVATION, LISTING, RESERVATION_CANCEL, CANCEL, EXPIRED }