package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-message")
data class PrivateChatMessage(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    val id: ObjectId,

    @JsonProperty("chatRoom")
    @Column(name = "chatRoom", length = 100)
    val privateChatRoom: PrivateChatRoom,

    @JsonProperty("sender")
    @Column(name = "sender", length = 100)
    val sender: User,

    @JsonProperty("message")
    @Column(name = "message", length = 100)
    val message: String = "",
)
