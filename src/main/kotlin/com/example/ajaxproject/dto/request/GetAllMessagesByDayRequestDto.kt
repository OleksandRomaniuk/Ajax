package com.example.ajaxproject.dto.request

import org.springframework.data.domain.Pageable

data class GetAllMessagesByDayRequestDto(
    val chatRoomId: String,
    val pageable: Pageable
)
