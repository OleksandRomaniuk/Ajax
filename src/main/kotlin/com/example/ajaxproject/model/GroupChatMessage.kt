package com.example.ajaxproject.model

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("group-chat-message")
data class GroupChatMessage(
    @Id
    val id: String,
    val date: Date,
    val groupChatRoom: String,
    val senderId: String,
    val message: String,
)
