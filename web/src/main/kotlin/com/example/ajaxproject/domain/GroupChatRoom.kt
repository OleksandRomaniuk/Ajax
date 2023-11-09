package com.example.ajaxproject.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("group-chat-room")
data class GroupChatRoom(
    @Id
    val id: String,
    val adminId: String,
    val chatName: String,
    var chatMembers: List<User>,
)
