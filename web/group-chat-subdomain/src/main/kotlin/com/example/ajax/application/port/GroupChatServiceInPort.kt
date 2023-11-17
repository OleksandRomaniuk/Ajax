package com.example.ajax.application.port

import com.example.ajax.domain.GroupChatRoom
import com.example.ajax.domain.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GroupChatServiceInPort {

    fun createGroupRoom(chatName: String, adminId: String): Mono<GroupChatRoom>

    fun getAllChatMembers(chatId: String): Flux<User>

    fun addUserToChat(chatId: String, userId: String): Mono<GroupChatRoom>

    fun leaveGroupChat(userId: String, chatId: String): Mono<String>
}
