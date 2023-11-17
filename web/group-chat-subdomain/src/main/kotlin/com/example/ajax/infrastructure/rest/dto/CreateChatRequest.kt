package com.example.ajax.infrastructure.rest.dto

data class CreateChatRequest(
    val adminId: String,
    val chatName: String,
)
