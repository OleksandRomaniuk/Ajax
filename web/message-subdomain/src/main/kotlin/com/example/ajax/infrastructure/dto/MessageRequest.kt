package com.example.ajax.infrastructure.dto

data class MessageRequest(
    val senderId: String,
    override val chatRoomId: String,
    val message: String
) : Identifiable

interface Identifiable {
    val chatRoomId: String
}
