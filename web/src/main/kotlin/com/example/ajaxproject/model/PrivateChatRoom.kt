package com.example.ajaxproject.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-room")
data class PrivateChatRoom(
    @Id
    val id: String,
    val senderId: String,
    val recipientId: String,
)
