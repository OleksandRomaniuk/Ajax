package com.example.ajaxproject.model

import jakarta.persistence.Column
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-message")
data class PrivateChatMessage(

    @Id
    val id: ObjectId,

    @Column(name = "chatRoom", length = 100)
    val privateChatRoom: PrivateChatRoom,

    @Column(name = "sender", length = 100)
    val sender: User,

    @Column(name = "message", length = 100)
    val message: String = "",
)
