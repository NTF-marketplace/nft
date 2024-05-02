package com.api.nft.event

import com.api.nft.domain.nft.Nft
import org.springframework.context.ApplicationEvent

data class NftCreatedEvent(val eventSource: Any, val nft: Nft): ApplicationEvent(eventSource)

