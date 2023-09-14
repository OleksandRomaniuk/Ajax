package com.example.ajaxproject.model

import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "users")
data class User(

    @Id
    val id: String,

    val email: String = "",
    val password: String = "",
)
