package com.example.ajaxproject.dto.responce

import java.util.*

data class GroupChatMessageResponse(
    val id: String,
    val senderId: String,
    val roomId: String,
    val message: String,
    val date: Date
)
