package com.example.ajax.application.service

import com.example.ajax.domain.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomService {

    fun createPrivateRoom(senderId: String, recipientId: String): Mono<PrivateChatRoom>

    fun getPrivateRoom(roomId: String): Mono<PrivateChatRoom>

}
