package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User
import com.example.ajaxproject.model.enums.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findUserByRole(role: Role): List<User>

}
