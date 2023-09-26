package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatMessage


interface GroupChatMessageRepository{

    fun save(chatMessage: GroupChatMessage): GroupChatMessage

    fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage>
}
