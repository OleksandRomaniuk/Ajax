package com.example.ajaxproject.dto.request

data class GroupChatIdentifiable(
    val senderId: String,
    val message: String,
    override val chatId: String
) : Identifiable
interface Identifiable {
    val chatId: String
}
