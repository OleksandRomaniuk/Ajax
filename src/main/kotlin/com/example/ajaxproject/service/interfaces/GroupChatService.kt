package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDTO
import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.model.GroupChatRoom

interface GroupChatService {
    fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom
    fun getAllChatMembers(chatId: String): List<String>
    fun addUserToChat(chatId: String, userId: String): List<String>
    fun sendMessageToGroup(groupChatDto: GroupChatDTO): GroupChatMessage
    fun getAllGroupMessages(chatId: String): List<GroupChatMessage>
    fun leaveGroupChat(chatId: String, userId: String): Boolean
}
