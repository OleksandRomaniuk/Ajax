package com.example.ajaxproject.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("private-chat-room")
data class PrivateChatRoom(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = "",

    @Column(name = "senderId", length = 100)
    val senderId: String,

    @Column(name = "recipientId", length = 100)
    val recipientId: String,
)
