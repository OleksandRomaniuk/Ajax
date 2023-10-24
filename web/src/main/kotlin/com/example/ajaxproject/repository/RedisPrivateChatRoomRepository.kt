package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatRoom
import reactor.core.publisher.Mono

interface RedisPrivateChatRoomRepository {

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>

    fun findById(chatRoomId: String): Mono<PrivateChatRoom>
}
