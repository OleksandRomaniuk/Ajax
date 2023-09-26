package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatRoom

interface PrivateChatRoomRepository{

    fun findChatRoomById(roomId: String): PrivateChatRoom?

    fun save(privateChatRoom: PrivateChatRoom): PrivateChatRoom
}
