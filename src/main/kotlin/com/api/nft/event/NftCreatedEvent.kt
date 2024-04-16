package com.api.nft.event

import com.api.nft.domain.nft.Nft
import org.springframework.context.ApplicationEvent

class NftCreatedEvent(source: Any, val nft: Nft): ApplicationEvent(source)