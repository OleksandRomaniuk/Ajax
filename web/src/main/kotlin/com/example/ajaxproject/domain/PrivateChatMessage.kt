package com.example.ajaxproject.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-message")
data class PrivateChatMessage(
    @Id
    val id: ObjectId,
    val privateChatRoomId: String,
    val senderId: String,
    val message: String,
)
