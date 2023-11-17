package com.example.ajax.infrastructure.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class RedisPrivateChatRoom(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("senderId")
    val senderId: String,
    @JsonProperty("recipientId")
    val recipientId: String
)
