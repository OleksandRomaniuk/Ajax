package com.example.ajaxproject.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("group-chat-message")
data class GroupChatMessage(
    @Id
    val id: String,
    val date: Date,
    val groupChatRoom: String,
    val senderId: String,
    val message: String,
)
