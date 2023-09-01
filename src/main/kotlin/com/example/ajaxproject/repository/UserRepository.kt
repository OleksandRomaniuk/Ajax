package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : GenericRepository<User, Long>
