package com.example.ajax.infractucture.redis.repository

import com.example.ajax.domain.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomRedisRepository {

    fun findById(id: String): Mono<PrivateChatRoom>

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>
}
