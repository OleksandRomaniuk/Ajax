package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatRoom

fun interface CustomRoomRepository {

    fun findChatRoom(chatId: String): GroupChatRoom

}
