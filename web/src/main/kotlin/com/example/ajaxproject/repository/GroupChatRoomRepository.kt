package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatRoom
import reactor.core.publisher.Mono


interface GroupChatRoomRepository{

    fun findChatRoom(chatId: String): Mono<GroupChatRoom>

    fun save(chat: GroupChatRoom): Mono<GroupChatRoom>

    fun removeUserFromAllChats(userId: String)
}
