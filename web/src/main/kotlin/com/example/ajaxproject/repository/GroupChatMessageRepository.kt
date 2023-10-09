package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatMessage
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux

interface GroupChatMessageRepository{

    fun save(chatMessage: GroupChatMessage): GroupChatMessage

    fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage>

     fun findMessagesByChatRoomIdWithPagination(chatRoomId: String, offset: Int, limit: Int)
        :Pair<List<GroupChatMessage>, Long>

     fun findAllMessagesInChatFlux(groupChatRoomId: String): Flux<GroupChatMessage>


    fun findMessagesByChatRoomIdWithPaginationFlux(
        chatRoomId: String,
        offset: Int,
        limit: Int
    ): Flux<GroupChatMessage>
}
