package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatRoom
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupChatRoomRepository : MongoRepository<GroupChatRoom, ObjectId>, CustomRoomRepository
