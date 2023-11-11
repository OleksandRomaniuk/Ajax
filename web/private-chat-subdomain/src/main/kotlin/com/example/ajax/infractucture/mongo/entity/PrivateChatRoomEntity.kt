package com.example.ajax.infractucture.mongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-room")
data class PrivateChatRoomEntity(
    @Id
    val id: String,
    val senderId: String,
    val recipientId: String
)

