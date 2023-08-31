package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chatroom")
data class ChatRoom(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String = "",

    @JsonProperty("senderId")
    @Column(name = "senderId", length = 100)
    val senderId: Int? = null,

    @JsonProperty("recipientId")
    @Column(name = "recipientId", length = 100)
    val recipientId: Int? = null,

    @JsonProperty("message")
    @Column(name = "message", length = 100)
    val message: String = "",

    @JsonProperty("timeStamp")
    @Column(name = "timeStamp", length = 100)
    val timeStamp: LocalDateTime? = null)