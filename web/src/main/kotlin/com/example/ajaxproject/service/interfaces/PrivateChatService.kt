package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.PrivateChatRoomRequest
import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PrivateChatService {

    fun createPrivateRoom(senderId: String, recipientId: String): Mono<PrivateChatRoom>

    fun getPrivateRoom(roomId: String): Mono<PrivateChatRoom>

    fun sendPrivateMessage(privateMessageDTO: PrivateMessageDTO): Mono<PrivateChatMessage>

    fun getAllPrivateMessages(privateChatRoomRequest: PrivateChatRoomRequest): Flux<PrivateChatMessage>
}
