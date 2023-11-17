package com.example.ajax.application.svc

import com.example.ajax.application.port.ChatMessageServiceInPort
import com.example.ajax.domain.ChatMessage
import com.example.ajax.domain.exception.NotFoundException
import com.example.ajax.application.port.Notification
import com.example.ajax.application.port.ChatMessageRepositoryOutPort
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class ChatMessageService(
    private val privateChatMessageRepositoryOutPort: ChatMessageRepositoryOutPort,
) : ChatMessageServiceInPort {

    @Notification
    override fun sendMessage(messageRequest: ChatMessage): Mono<ChatMessage> {
        val privateChatMessage = ChatMessage(
            id = ObjectId().toHexString(),
            date = Date(),
            chatRoomId = messageRequest.chatRoomId,
            message = messageRequest.message,
            senderId = messageRequest.senderId
        )
        return privateChatMessageRepositoryOutPort.save(privateChatMessage)
            .doOnNext { logger.info("Send private message {}", it.id) }
    }

    override fun getAllMessages(roomId: String): Flux<ChatMessage> {
        return privateChatMessageRepositoryOutPort.findAllByChatRoomId(roomId)
            .switchIfEmpty(Flux.error { NotFoundException("Message between users not found") })
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ChatMessageServiceInPort::class.java)
    }
}
