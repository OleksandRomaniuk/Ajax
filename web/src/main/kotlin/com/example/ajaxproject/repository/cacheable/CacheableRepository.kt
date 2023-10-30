package com.example.ajaxproject.repository.cacheable

import com.example.ajaxproject.model.PrivateChatRoom
import reactor.core.publisher.Mono

interface CacheableRepository{// TODO: Make as Generic interface to avoid code duplication for possible future use

    fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom>

    fun findById(chatRoomId: String): Mono<PrivateChatRoom>

}
