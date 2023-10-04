package com.example.ajaxproject.dto.request

data class PrivateMessageDTO(
    val senderId: String,
    val recipientId: String,
    val message: String
)
