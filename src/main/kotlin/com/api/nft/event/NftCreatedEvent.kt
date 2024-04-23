package com.api.nft.event

import com.api.nft.domain.nft.Nft
import org.springframework.context.ApplicationEvent

data class NftCreatedEvent(val source: Any, val nft: Nft): ApplicationEvent(source)
