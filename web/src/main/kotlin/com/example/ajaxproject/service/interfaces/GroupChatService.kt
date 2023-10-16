package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.CreateChatDTO
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GroupChatService {

    fun createGroupRoom(createChatDto: CreateChatDTO): Mono<GroupChatRoom>

    fun getAllChatMembers(chatId: String): Mono<List<User>>

    fun addUserToChat(chatId: String, userId: String): Mono<User>

    fun sendMessageToGroup(groupChatDto: GroupChatDto): Mono<GroupChatMessageResponse>

    fun getAllGroupMessages(chatId: String): Mono<List<GroupChatMessageResponse>>

    fun leaveGroupChat(userId: String, chatId: String): Mono<Boolean>
}
