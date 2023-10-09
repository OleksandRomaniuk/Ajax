package com.example.ajaxproject.dto.responce

data class SendEmailResponse(
    val status: Int = 0,
    val cause: String? = null,
    val exception: Exception? = null
)
