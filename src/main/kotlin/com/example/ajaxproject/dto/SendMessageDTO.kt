package com.example.ajaxproject.dto

data class SendMessageDTO(

    val senderId: Long,

    val recipientId: Long,

    val message: String)
