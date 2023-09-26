package com.example.ajaxproject.model

import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("group-chat-room")
data class GroupChatRoom(

    @Id
    val id: ObjectId,

    val adminId: String,
    val chatName: String,
    var chatMembers: List<User>,
)
