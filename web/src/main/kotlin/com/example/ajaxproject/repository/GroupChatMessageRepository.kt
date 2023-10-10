package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatMessage

interface GroupChatMessageRepository{

    fun save(chatMessage: GroupChatMessage): GroupChatMessage

    fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage>

     fun findMessagesByChatRoomIdWithPagination(chatRoomId: String, offset: Int, limit: Int)
        :Pair<List<GroupChatMessage>, Long>
}
