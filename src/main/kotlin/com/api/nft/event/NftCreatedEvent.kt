package com.api.nft.event

import com.api.nft.service.dto.NftResponse
import org.springframework.context.ApplicationEvent

data class NftCreatedEvent(val eventSource: Any, val nft: NftResponse): ApplicationEvent(eventSource)

