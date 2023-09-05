package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.RoomDTO
import com.example.ajaxproject.dto.SendMessageDTO
import com.example.ajaxproject.model.ChatMessage
import com.example.ajaxproject.model.ChatRoom

interface ChatService {

    fun createRoom(senderId: Long, recipientId: Long): ChatRoom

    fun getRoom(roomId: String): ChatRoom?

    fun sendMessage(sendMessageDTO: SendMessageDTO): ChatMessage

    fun getAllMessages(roomDTO: RoomDTO): List<ChatMessage>

}
