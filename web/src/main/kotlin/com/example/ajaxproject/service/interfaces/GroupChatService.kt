package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.dto.responce.OffsetPaginateResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface GroupChatService {
         fun createGroupRoom(createChatDto: CreateChatDto): Mono<GroupChatRoom>
         fun getAllChatMembers(chatId: String): Flux<User>
         fun addUserToChat(chatId: String, userId: String): Mono<List<User>>
         fun sendMessageToGroup(groupChatDto: GroupChatDto): Mono<GroupChatMessageResponse>
         fun getAllGroupMessages(chatId: String): Flux<GroupChatMessageResponse>
         fun leaveGroupChat(chatId: String, userId: String): Mono<Boolean>
}
