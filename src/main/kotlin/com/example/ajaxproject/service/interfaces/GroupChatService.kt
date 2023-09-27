package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDTO
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.impl.DayMessages
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GroupChatService {

    fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom

    fun getAllChatMembers(chatId: String): List<User>

    fun addUserToChat(chatId: String, userId: String): List<User>

    fun sendMessageToGroup(groupChatDto: GroupChatDTO): GroupChatMessageResponse

     fun findAllMessagesByDay(groupChatRoomId: String, pageable: Pageable): Page<DayMessages>

    fun leaveGroupChat(chatId: String, userId: String): Boolean
}
