package com.example.ajax.application.service

import com.example.ajax.domain.ChatMessage
import com.example.ajax.exception.NotFoundException
import com.example.ajax.infractucture.bpp.Notification
import com.example.ajax.infractucture.dto.MessageRequest
import com.example.ajax.infractucture.mongo.repository.ChatMessageRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class ChatMessageService(
    private val privateChatMessageRepository: ChatMessageRepository,
) : ChatMessageServiceOutPort {

    @Notification
    override fun sendMessage(messageRequest: MessageRequest): Mono<ChatMessage> {
        val privateChatMessage = ChatMessage(
            id = ObjectId().toHexString(),
            date = Date(),
            chatRoomId = messageRequest.chatRoomId,
            message = messageRequest.message,
            senderId = messageRequest.senderId
        )
        return privateChatMessageRepository.save(privateChatMessage)
            .doOnNext { logger.info("Send private message {}", it.id) }
    }

    override fun getAllMessages(roomId: String): Flux<ChatMessage> {
        return privateChatMessageRepository.findAllByChatRoomId(roomId)
            .switchIfEmpty(Flux.error { NotFoundException("Message between users not found") })
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ChatMessageServiceOutPort::class.java)
    }
}
