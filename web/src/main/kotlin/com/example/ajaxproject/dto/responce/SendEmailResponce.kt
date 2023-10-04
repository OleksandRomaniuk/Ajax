package com.example.ajaxproject.dto.responce

data class SendEmailResponce(
    val status: Int = 0,
    val cause: String? = null,
    val exception: Exception? = null
)
