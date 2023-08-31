package com.example.ajaxproject.model.dto

import com.example.ajaxproject.model.Enum.Role

data class UserDtoResponse(
    val id: Long,
    val firstName: String,
    val secondName: String,
    val role: Role,
)