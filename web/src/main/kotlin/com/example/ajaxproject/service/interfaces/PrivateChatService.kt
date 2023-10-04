package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.RoomDTO
import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom

interface PrivateChatService {

    fun createPrivateRoom(senderId: String, recipientId: String): PrivateChatRoom

    fun getPrivateRoom(roomId: String): PrivateChatRoom?

    fun sendPrivateMessage(privateMessageDTO: PrivateMessageDTO): PrivateChatMessage

    fun getAllPrivateMessages(roomDTO: RoomDTO): List<PrivateChatMessage>
}
