package com.example.ajaxproject.domain

import com.example.ajaxproject.dto.responce.PrivateChatRoomResponse
import com.example.ajaxproject.domain.mongo.MongoPrivateChatRoom
import com.example.ajaxproject.domain.redis.RedisPrivateChatRoom

data class PrivateChatRoom(
    val id: String,
    val senderId: String,
    val recipientId: String
)

fun PrivateChatRoom.toResponse(): PrivateChatRoomResponse {
    return PrivateChatRoomResponse(
        id = this.id,
        senderId = this.senderId,
        recipientId = this.recipientId
    )
}

fun PrivateChatRoom.toRedis(): RedisPrivateChatRoom {
    return RedisPrivateChatRoom(
        id = this.id,
        senderId = this.senderId,
        recipientId = this.recipientId
    )
}

fun PrivateChatRoom.toMongo(): MongoPrivateChatRoom {
    return MongoPrivateChatRoom(
        id = this.id,
        senderId = this.senderId,
        recipientId = this.recipientId
    )
}
