package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface PrivateChatMessageRepository{
    fun findAllByPrivateChatRoomId(chatRoomId: String): List<PrivateChatMessage>
    abstract fun save(privateChatMessage: PrivateChatMessage)
}
