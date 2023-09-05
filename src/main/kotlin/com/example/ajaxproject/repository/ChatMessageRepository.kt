package com.example.ajaxproject.repository

import com.example.ajaxproject.model.ChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : MongoRepository<ChatMessage, String>{

    fun findAllByChatRoomId(chatRoomId: String): List<ChatMessage>

}
