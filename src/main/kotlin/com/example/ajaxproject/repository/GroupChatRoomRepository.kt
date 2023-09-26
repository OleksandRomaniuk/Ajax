package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatRoom


interface GroupChatRoomRepository{

    fun findChatRoom(chatId: String): GroupChatRoom

    fun save(chat: GroupChatRoom): GroupChatRoom

    fun validateAndCleanChatMembers(chatRoomId: String)
}
