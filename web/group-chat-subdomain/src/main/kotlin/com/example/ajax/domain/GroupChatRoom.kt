package com.example.ajax.domain

data class GroupChatRoom(
    val id: String,
    val adminId: String,
    val chatName: String,
    var chatMembers: List<User>,
)
