package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User
import com.example.ajaxproject.model.enums.Role
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : GenericRepository<User, Long>{
    fun findByEmail(email: String): User?
    fun findUserByRole(role: Role): List<User>
}
