package com.example.ajaxproject.repository.mongo

import com.example.ajaxproject.model.PrivateChatMessage
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PrivateChatMessageRepository{

    fun findAllByPrivateChatRoomId(chatRoomId: String): Flux<PrivateChatMessage>

    fun save(privateChatMessage: PrivateChatMessage): Mono<PrivateChatMessage>
}
