package com.example.ajaxproject.dto.request

data class GroupChatDTO(
    val senderId: Long,
    val message: String,
    val chatId: String
)
