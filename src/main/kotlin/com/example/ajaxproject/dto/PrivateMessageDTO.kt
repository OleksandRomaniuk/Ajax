package com.example.ajaxproject.dto

data class PrivateMessageDTO(
    val senderId: Long,
    val recipientId: Long,
    val message: String
)
