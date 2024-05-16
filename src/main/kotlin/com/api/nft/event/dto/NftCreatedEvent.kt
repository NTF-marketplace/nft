package com.api.nft.event.dto

import org.springframework.context.ApplicationEvent

data class NftCreatedEvent(val eventSource: Any, val nft: NftResponse): ApplicationEvent(eventSource)

