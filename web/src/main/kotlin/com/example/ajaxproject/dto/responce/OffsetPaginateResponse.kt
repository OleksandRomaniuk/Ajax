package com.example.ajaxproject.dto.responce

import com.example.ajaxproject.model.GroupChatMessage

data class OffsetPaginateResponse(
    val data: List<GroupChatMessage>,
    val totalCount: Long
)
