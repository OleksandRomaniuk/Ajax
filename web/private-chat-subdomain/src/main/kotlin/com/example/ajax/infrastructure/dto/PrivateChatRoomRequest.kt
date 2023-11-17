package com.example.ajax.infrastructure.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PrivateChatRoomRequest @JsonCreator constructor(
    @JsonProperty("senderId") val senderId: String,
    @JsonProperty("recipientId") val recipientId: String
)
