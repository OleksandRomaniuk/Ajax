package com.example.ajaxproject.dto.responce

import org.bson.types.ObjectId

data class GroupChatMessageResponse(
    val id: ObjectId,
    val senderId: String,
    val roomId: ObjectId,
    val message: String
)
