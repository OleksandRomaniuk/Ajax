package com.example.ajaxproject.dto.request

data class PrivateMessageDTO(
    val senderId: Long,
    val recipientId: Long,
    val message: String
)
