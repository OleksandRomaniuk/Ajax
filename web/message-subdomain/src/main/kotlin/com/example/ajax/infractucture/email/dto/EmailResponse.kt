package com.example.ajax.infractucture.email.dto

data class EmailResponse(
    val status: Int = 0,
    val cause: String? = null,
    val exception: Exception? = null
)
