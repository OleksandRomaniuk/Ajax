package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("chat-message")
data class ChatMessage(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO)
    val id: ObjectId ,

    @JsonProperty("chatRoom")
    @Column(name = "chatRoom", length = 100)
    val chatRoom: ChatRoom? = null,

    @JsonProperty("message")
    @Column(name = "message", length = 100)
    val message: String = "",

    @JsonProperty("timeStamp")
    @Column(name = "timeStamp", length = 100)
    val timeStamp: LocalDateTime? = null)

