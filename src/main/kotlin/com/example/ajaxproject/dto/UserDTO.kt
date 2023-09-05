package com.example.ajaxproject.dto

import com.example.ajaxproject.model.enums.Role

class UserDTO(
    val firstName: String,

    val secondName: String,

    val email: String,

    val password: String,

    val role: Role)

