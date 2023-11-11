package com.example.ajax.application.service

import com.example.ajax.domain.GroupChatRoom
import com.example.ajax.domain.User
import com.example.ajax.infractucture.rest.dto.CreateChatRequest
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GroupChatServiceOutPort {

    fun createGroupRoom(createChatRequest: CreateChatRequest): Mono<GroupChatRoom>

    fun getAllChatMembers(chatId: String): Flux<User>

    fun addUserToChat(chatId: String, userId: String): Mono<GroupChatRoom>

    fun leaveGroupChat(userId: String, chatId: String): Mono<String>
}
