package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDTO
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User

interface GroupChatService {
    fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom
    fun getAllChatMembers(chatId: String): List<User>
    fun addUserToChat(chatId: String, userId: String): List<User>
    fun sendMessageToGroup(groupChatDto: GroupChatDTO): GroupChatMessageResponse
    fun getAllGroupMessages(chatId: String): List<GroupChatMessageResponse>
    fun leaveGroupChat(chatId: String, userId: String): Boolean
}
