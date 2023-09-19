package com.example.ajaxproject.email

data class Email(
    val from: String,
    val to: String,
    val subject: String? = null,
    val body: String
)
