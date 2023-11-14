package com.example.ajax.application

import com.example.ajax.domain.GroupChatRoom
import reactor.core.publisher.Mono

interface GroupChatRoomRepositoryOutPort {

    fun findChatRoom(chatId: String): Mono<GroupChatRoom>

    fun save(chat: GroupChatRoom): Mono<GroupChatRoom>

    fun removeUserFromAllChats(userId: String): Mono<Unit>
}
