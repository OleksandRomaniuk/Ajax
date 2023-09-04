package com.example.ajaxproject.repository

import com.example.ajaxproject.model.Mentors

interface MentorsRepository : GenericRepository<Mentors, Long>{

    fun findByUserId(userId: Long): Mentors?

}
