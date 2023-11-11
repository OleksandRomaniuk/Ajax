package com.example.ajax.application.service

import com.example.ajax.domain.ChatMessage
import com.example.ajax.infractucture.dto.MessageRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ChatMessageServiceOutPort {

    fun sendMessage(messageRequest: MessageRequest): Mono<ChatMessage>

    fun getAllMessages(roomId: String): Flux<ChatMessage>
}
