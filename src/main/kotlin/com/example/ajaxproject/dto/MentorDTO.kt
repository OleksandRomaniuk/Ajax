package com.example.ajaxproject.dto

import com.example.ajaxproject.model.enums.Role

data class MentorDTO(
    val email: String,
    val firstName: String,
    val secondName: String,
    val role: Role,
    val description: String?,
    val isOnline: Boolean,
    val isOfflineIn: Boolean,
    val rating: Double
)
