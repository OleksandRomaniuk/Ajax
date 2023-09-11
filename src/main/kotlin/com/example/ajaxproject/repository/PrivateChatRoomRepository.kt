package com.example.ajaxproject.repository

import com.example.ajaxproject.model.PrivateChatRoom
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PrivateChatRoomRepository : MongoRepository<PrivateChatRoom, String>
