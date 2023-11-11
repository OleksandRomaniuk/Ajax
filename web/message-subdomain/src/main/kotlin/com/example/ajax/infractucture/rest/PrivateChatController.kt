package com.example.ajax.infractucture.rest

import com.example.ajax.application.service.ChatMessageServiceOutPort
import com.example.ajax.domain.ChatMessage
import com.example.ajax.infractucture.dto.MessageRequest
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
    val chatMessageServiceOutPort: ChatMessageServiceOutPort
) {

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody privateMessageRequest: MessageRequest): Mono<ChatMessage> =
        chatMessageServiceOutPort.sendMessage(privateMessageRequest)

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody roomId: String): Flux<ChatMessage> =
        chatMessageServiceOutPort.getAllMessages(roomId)
}
