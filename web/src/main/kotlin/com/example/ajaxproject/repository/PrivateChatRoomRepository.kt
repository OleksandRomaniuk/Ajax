package com.example.ajaxproject.repository

import com.example.ajaxproject.domain.PrivateChatRoom
import reactor.core.publisher.Mono

interface PrivateChatRoomRepository{

    fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom>

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>
}
