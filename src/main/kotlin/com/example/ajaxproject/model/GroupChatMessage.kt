package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("group-chat-message")
data class GroupChatMessage(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO)
    val id: ObjectId,

    @Column(name = "groupChatRoom", length = 100)
    val groupChatRoom: GroupChatRoom,

    @Column(name = "sender", length = 100)
    val sender: User,

    @Column(name = "message", length = 100)
    val message: String = "",
)
