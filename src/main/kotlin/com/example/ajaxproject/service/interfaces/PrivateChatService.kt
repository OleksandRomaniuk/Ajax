package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.RoomDTO
import com.example.ajaxproject.dto.PrivateMessageDTO
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom

interface PrivateChatService {
    fun createPrivateRoom(senderId: Long, recipientId: Long): PrivateChatRoom

    fun getPrivateRoom(roomId: String): PrivateChatRoom?

    fun sendPrivateMessage(privateMessageDTO: PrivateMessageDTO): PrivateChatMessage

    fun getAllPrivateMessages(roomDTO: RoomDTO): List<PrivateChatMessage>

}
