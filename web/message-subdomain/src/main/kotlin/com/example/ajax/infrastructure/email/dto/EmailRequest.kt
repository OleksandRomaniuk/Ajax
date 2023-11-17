package com.example.ajax.infrastructure.email.dto

data class EmailRequest(
    val from: String,
    val to: String,
    val subject: String,
    val body: String
)
