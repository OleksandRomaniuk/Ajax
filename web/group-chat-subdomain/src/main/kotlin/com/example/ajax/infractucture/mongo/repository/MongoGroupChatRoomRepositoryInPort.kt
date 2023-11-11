package com.example.ajax.infractucture.mongo.repository

import com.example.ajax.domain.GroupChatRoom
import reactor.core.publisher.Mono

interface MongoGroupChatRoomRepositoryInPort {

    fun findChatRoom(chatId: String): Mono<GroupChatRoom>

    fun save(chat: GroupChatRoom): Mono<GroupChatRoom>

    fun removeUserFromAllChats(userId: String): Mono<Unit>
}
