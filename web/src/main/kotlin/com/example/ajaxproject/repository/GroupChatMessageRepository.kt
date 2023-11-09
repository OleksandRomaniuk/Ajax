package com.example.ajaxproject.repository

import com.example.ajaxproject.domain.GroupChatMessage
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GroupChatMessageRepository{

    fun save(chatMessage: GroupChatMessage): Mono<GroupChatMessage>

    fun findAllMessagesInChat(groupChatRoomId: String): Flux<GroupChatMessage>
}
