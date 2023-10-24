package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-room")
data class PrivateChatRoom @JsonCreator constructor(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("recipientId")
    val recipientId: String
)

