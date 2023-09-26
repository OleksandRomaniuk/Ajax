package com.example.ajaxproject.dto.request


data class GroupChatDTO(
    val senderId: String,
    val message: String,
    override val chatId: String
) : ChatDTO

interface ChatDTO {
    val chatId: String
}
