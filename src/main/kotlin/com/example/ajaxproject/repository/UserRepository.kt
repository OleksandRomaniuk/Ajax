package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User

interface UserRepository{
    fun save(user: User): User
    fun findByEmail(email: String): User?
    fun findUserById(userId: String): User?
    fun findAll(): List<User>
    fun deleteById(userId: String)
}
