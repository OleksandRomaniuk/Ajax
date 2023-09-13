package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("group-chat-room")
data class GroupChatRoom(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    val id: ObjectId,

    @JsonProperty("adminId")
    @Column(name = "adminId", length = 100)
    val adminId: String,

    @JsonProperty("chatName")
    @Column(name = "chatName")
    val chatName: String = "",

    @JsonProperty("chatMembers")
    @Column(name = "chatMembers")
    var chatMembers: List<User>,
)
