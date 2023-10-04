package com.example.ajaxproject.dto.request

data class EmailDTO(
    val from: String,
    val to: String,
    val subject: String? = null,
    val body: String
)
