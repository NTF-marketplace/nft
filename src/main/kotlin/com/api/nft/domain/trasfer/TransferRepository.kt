package com.api.nft.domain.trasfer

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface TransferRepository: ReactiveCrudRepository<Transfer,Long> {
}