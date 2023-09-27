package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.repository.impl.DayMessages
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface GroupChatMessageRepository{

    fun save(chatMessage: GroupChatMessage): GroupChatMessage

    fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage>

    fun findAllMessagesByDay(groupChatRoomId: String,pageable: Pageable): Page<DayMessages>
}
