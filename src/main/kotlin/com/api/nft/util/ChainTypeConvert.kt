package com.api.nft.util

import com.api.nft.enums.ChainType
import com.api.nft.enums.ContractType
import com.api.nft.enums.TokenType
import org.springframework.data.r2dbc.convert.EnumWriteSupport

data class ContractTypeConverter<T : Enum<T>>(private val enumType: Class<T>) : EnumWriteSupport<ContractType>()
data class ChainTypeConvert<T: Enum<T>>(private val enumType: Class<T>): EnumWriteSupport<ChainType>()
data class TokenTypeConvert<T: Enum<T>>(private val enumType: Class<T>): EnumWriteSupport<TokenType>()
