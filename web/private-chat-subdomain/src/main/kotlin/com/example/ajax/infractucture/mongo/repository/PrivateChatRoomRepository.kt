package com.example.ajax.infractucture.mongo.repository

import com.example.ajax.domain.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomRepository {

    fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom>

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>
}
