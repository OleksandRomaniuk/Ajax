package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.CreateChatDto
import com.example.ajaxproject.dto.GroupChatDTO
import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User

interface GroupChatService {

    fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom

    fun getAllChatMembers(chatId: String): List<User>

    fun addUserToChat(chatId:String , userId:Long): List<User>

    fun sendMessageToGroup(groupChatDto: GroupChatDTO): GroupChatMessage

    fun getAllGroupMessages(chatId: String): List<GroupChatMessage>

    fun leaveGroupChat(chatId: String, userId: Long): Boolean

}
