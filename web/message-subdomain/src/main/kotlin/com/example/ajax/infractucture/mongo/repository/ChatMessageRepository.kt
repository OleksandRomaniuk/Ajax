package com.example.ajax.infractucture.mongo.repository

import com.example.ajax.domain.ChatMessage
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ChatMessageRepository {

    fun findAllByChatRoomId(chatRoomId: String): Flux<ChatMessage>

    fun save(privateChatMessage: ChatMessage): Mono<ChatMessage>
}
