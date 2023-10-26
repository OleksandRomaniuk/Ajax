package com.example.ajaxproject.dto.responce

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PrivateChatRoomResponse @JsonCreator constructor(
    @JsonProperty("id") val id: String,
    @JsonProperty("senderId") val senderId: String,
    @JsonProperty("recipientId") val recipientId: String
)
