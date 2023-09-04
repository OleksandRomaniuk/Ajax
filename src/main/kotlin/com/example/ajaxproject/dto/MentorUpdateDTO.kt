package com.example.ajaxproject.dto

data class MentorUpdateDTO(
    val description: String?,
    val isOnline: Boolean,
    val isOfflineIn: Boolean,
    val rating: Double
)

