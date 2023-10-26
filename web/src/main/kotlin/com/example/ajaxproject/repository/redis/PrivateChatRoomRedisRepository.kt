package com.example.ajaxproject.repository.redis

import com.example.ajaxproject.model.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomRedisRepository {

    fun findById(id: String): Mono<PrivateChatRoom>

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>
}
