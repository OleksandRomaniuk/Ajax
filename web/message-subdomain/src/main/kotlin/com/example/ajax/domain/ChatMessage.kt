package com.example.ajax.domain

import java.util.*

data class ChatMessage(
    val id: String,
    val date: Date,
    val senderId: String,
    val message: String,
    val chatRoomId: String
)
