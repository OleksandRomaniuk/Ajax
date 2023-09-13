package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Id
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

@Document("group-chat-room")
data class GroupChatRoom(

    @Id
    @Column(name = "id")
    val id: ObjectId,

    @Column(name = "adminId", length = 100)
    val adminId: ObjectId,

    @Column(name = "chatName")
    val chatName: String = "",

    @Column(name = "chatMembers")
    var chatMembers: List<User>,
)
