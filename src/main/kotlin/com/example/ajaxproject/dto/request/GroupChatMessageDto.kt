package com.example.ajaxproject.dto.request

import com.example.ajaxproject.model.GroupChatMessage
import org.bson.types.ObjectId

data class GroupChatMessageDto(
    val id: ObjectId,
    val senderId: String,
    val message: String,
) {
    constructor(chatMessage: GroupChatMessage) : this(
        id = chatMessage.id,
        senderId = chatMessage.senderId,
        message = chatMessage.message
    )
}
