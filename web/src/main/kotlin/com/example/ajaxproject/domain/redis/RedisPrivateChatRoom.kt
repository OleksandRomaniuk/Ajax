package com.example.ajaxproject.domain.redis

import com.example.ajaxproject.domain.PrivateChatRoom
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
