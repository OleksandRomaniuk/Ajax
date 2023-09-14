package com.example.ajaxproject.model

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("group-chat-message")
data class GroupChatMessage(

    @Id
    val id: ObjectId,

    val groupChatRoom: GroupChatRoom,
    val senderId: String,
    val message: String,
)
