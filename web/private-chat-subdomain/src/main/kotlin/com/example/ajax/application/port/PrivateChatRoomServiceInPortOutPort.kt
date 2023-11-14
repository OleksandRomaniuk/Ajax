package com.example.ajax.application.port

import com.example.ajax.domain.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomServiceInPortOutPort {

    fun createPrivateRoom(senderId: String, recipientId: String): Mono<PrivateChatRoom>

    fun getPrivateRoom(roomId: String): Mono<PrivateChatRoom>

}
