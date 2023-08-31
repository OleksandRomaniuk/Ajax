package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "chatroom")
data class ChatMessage(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = "",

    @JsonProperty("senderId")
    @Column(name = "senderId", length = 100)
    val senderId: String? = null,

    @JsonProperty("recipientId")
    @Column(name = "recipientId", length = 100)
    val recipientId: String? = null,

)