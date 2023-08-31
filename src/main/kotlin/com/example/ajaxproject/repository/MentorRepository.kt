package com.example.ajaxproject.repository

import com.example.ajaxproject.model.Mentors
import com.example.ajaxproject.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface MentorRepository : CrudRepository<Mentors, Int>