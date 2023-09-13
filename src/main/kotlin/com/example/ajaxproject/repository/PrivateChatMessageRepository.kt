package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PrivateChatMessageRepository : MongoRepository<PrivateChatMessage, String> {
    fun findAllByPrivateChatRoomId(chatRoomId: String): List<PrivateChatMessage>

}
