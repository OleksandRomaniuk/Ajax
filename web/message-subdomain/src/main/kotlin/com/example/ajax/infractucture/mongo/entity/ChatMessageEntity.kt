package com.example.ajax.infractucture.mongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("chat-message")
data class ChatMessageEntity(
    @Id
    val id: String,
    val date: Date,
    val chatRoomId: String,
    val senderId: String,
    val message: String,
)
