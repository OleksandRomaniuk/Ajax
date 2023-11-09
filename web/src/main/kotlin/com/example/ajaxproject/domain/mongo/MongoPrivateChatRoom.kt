package com.example.ajaxproject.domain.mongo

import com.example.ajaxproject.domain.PrivateChatRoom
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-room")
data class MongoPrivateChatRoom(
    @Id
    val id: String,
    val senderId: String,
    val recipientId: String
)

fun MongoPrivateChatRoom.toDomain(): PrivateChatRoom {
    return PrivateChatRoom(
        id = this.id,
        senderId = this.senderId,
        recipientId = this.recipientId
    )
}
