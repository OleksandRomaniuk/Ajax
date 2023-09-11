package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.model.PrivateChatMessage
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupChatMessageRepository : MongoRepository<GroupChatMessage, ObjectId>
