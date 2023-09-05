package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("chat-room")
data class ChatRoom(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = "",

    @JsonProperty("senderId")
    @Column(name = "senderId", length = 100)
    val senderId: Long? = null,

    @JsonProperty("recipientId")
    @Column(name = "recipientId", length = 100)
    val recipientId: Long? = null)
