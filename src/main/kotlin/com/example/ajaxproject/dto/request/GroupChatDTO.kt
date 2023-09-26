package com.example.ajaxproject.dto.request



data class GroupChatDTO(
    val senderId: String,
    val message: String,
    val chatId: String
)
