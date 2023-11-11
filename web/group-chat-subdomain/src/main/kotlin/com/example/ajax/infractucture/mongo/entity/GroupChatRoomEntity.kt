package com.example.ajax.infractucture.mongo.entity

import com.example.ajax.domain.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("groupChatRooms")
data class GroupChatRoomEntity(

    @Id
    val id: String,
    val adminId: String,
    val chatName: String,
    var chatMembers: List<User>,
)
