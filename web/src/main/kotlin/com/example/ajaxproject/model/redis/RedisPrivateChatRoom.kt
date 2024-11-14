package com.example.ajaxproject.model.redis

import com.example.ajaxproject.model.PrivateChatRoom
import com.fasterxml.jackson.annotation.JsonProperty

data class RedisPrivateChatRoom(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("recipientId")
    val recipientId: String
)

fun RedisPrivateChatRoom.toDomain(): PrivateChatRoom {
    return PrivateChatRoom(
        id = this.id,
        senderId = this.senderId,
        recipientId = this.recipientId
    )
}
