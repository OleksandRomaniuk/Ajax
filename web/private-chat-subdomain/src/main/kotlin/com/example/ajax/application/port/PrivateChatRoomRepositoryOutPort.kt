package com.example.ajax.application.port

import com.example.ajax.domain.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomRepositoryOutPort {

    fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom>

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>
}
