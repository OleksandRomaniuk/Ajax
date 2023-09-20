package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatMessage

interface PrivateChatMessageRepository{
    fun findAllByPrivateChatRoomId(chatRoomId: String): List<PrivateChatMessage>
    fun save(privateChatMessage: PrivateChatMessage): PrivateChatMessage
}
