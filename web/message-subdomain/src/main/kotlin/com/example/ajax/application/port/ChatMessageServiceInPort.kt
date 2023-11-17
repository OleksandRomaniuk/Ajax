package com.example.ajax.application.port

import com.example.ajax.domain.ChatMessage
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ChatMessageServiceInPort {

    fun sendMessage(messageRequest: ChatMessage): Mono<ChatMessage>

    fun getAllMessages(roomId: String): Flux<ChatMessage>
}
