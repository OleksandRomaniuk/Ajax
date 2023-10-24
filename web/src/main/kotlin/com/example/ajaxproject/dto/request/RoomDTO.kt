package com.example.ajaxproject.dto.request

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class RoomDTO @JsonCreator constructor(
    @JsonProperty("senderId") val senderId: String,
    @JsonProperty("recipientId") val recipientId: String
)
