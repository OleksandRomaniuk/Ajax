package com.example.ajaxproject.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-message")
data class PrivateChatMessage(
    @Id
    val id: ObjectId,
    val privateChatRoom: PrivateChatRoom,
    val senderId: String,
    val message: String,
)