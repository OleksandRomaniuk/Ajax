package com.example.ajaxproject.repository

import com.example.ajaxproject.model.Mentors
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MentorsRepository : JpaRepository<Mentors, Long>
