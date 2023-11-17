package com.example.ajax.infrastructure.mapper

import com.example.ajax.domain.ChatMessage
import com.example.ajax.infrastructure.configuration.mapper.EntityMapper
import com.example.ajax.infrastructure.dto.MessageRequest
import com.example.ajax.infrastructure.mongo.entity.ChatMessageEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChatMessageMapper : EntityMapper<ChatMessage, ChatMessageEntity> {

    override fun mapToDomain(entity: ChatMessageEntity): ChatMessage {
        return ChatMessage(
            id = entity.id,
            date = entity.date,
            chatRoomId = entity.chatRoomId,
            senderId = entity.senderId,
            message = entity.message
        )
    }

    override fun mapToEntity(domain: ChatMessage): ChatMessageEntity {
        return ChatMessageEntity(
            id = domain.id,
            date = domain.date,
            chatRoomId = domain.chatRoomId,
            senderId = domain.senderId,
            message = domain.message
        )
    }

    fun mapToChatMessage(request: MessageRequest): ChatMessage {
        return ChatMessage(
            id = "",
            date = Date(),
            senderId = request.senderId,
            message = request.message,
            chatRoomId = request.chatRoomId
        )
    }
}
