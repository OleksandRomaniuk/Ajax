package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatRoom
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface PrivateChatRoomRepository{
    abstract fun findById(roomId: String): Any
    abstract fun save(privateChatRoom: PrivateChatRoom)

}
