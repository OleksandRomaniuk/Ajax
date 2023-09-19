package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User

fun interface CustomUserRepository {
    fun findUser(userId: String): User
}
