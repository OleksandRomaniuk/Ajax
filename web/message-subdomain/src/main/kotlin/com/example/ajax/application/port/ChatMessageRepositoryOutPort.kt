package com.example.ajax.application.port

import com.example.ajax.domain.ChatMessage
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ChatMessageRepositoryOutPort {

    fun findAllByChatRoomId(chatRoomId: String): Flux<ChatMessage>

    fun save(privateChatMessage: ChatMessage): Mono<ChatMessage>
}
