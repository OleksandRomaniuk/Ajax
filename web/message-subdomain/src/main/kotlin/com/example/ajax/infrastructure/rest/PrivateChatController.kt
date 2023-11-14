package com.example.ajax.infrastructure.rest

import com.example.ajax.application.port.ChatMessageServiceInPort
import com.example.ajax.domain.ChatMessage
import com.example.ajax.infrastructure.dto.MessageRequest
import com.example.ajax.infrastructure.mapper.ChatMessageMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chat")
class PrivateChatController(
    val chatMessageServiceInPort: ChatMessageServiceInPort,
    private val chatMessageMapper: ChatMessageMapper
) {

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody privateMessageRequest: MessageRequest): Mono<ChatMessage> =
        chatMessageServiceInPort.sendMessage(chatMessageMapper.mapToChatMessage(privateMessageRequest))

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody roomId: String): Flux<ChatMessage> =
        chatMessageServiceInPort.getAllMessages(roomId)
}
