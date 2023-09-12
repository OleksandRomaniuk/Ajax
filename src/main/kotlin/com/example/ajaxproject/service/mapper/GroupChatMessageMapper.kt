package com.example.ajaxproject.service.mapper

import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.model.GroupChatMessage
import org.springframework.stereotype.Service

@Service
class GroupChatMessageMapper {
    fun toResponseDto(chatMessage: GroupChatMessage): GroupChatMessageResponse {
        return GroupChatMessageResponse(
            id = chatMessage.id,
            senderId = chatMessage.sender.id,
            roomId = chatMessage.groupChatRoom.id,
            message = chatMessage.message
        )
    }
}
