package com.example.ajaxproject.dto.responce

import org.bson.types.ObjectId

data class GetAllMessagesByDayResponseDto(
    val content: List<DayMessagesDto>,
    val totalPages: Int,
    val totalElements: Long
)

data class DayMessagesDto(
    val day: String,
    val messages: List<GroupChatMessageDto>
)

data class GroupChatMessageDto(
    val id: ObjectId,
    val senderId: String,
    val roomId: ObjectId,
    val message: String,
)
