package com.example.ajaxproject.repository

import com.example.ajaxproject.model.Categories
import com.example.ajaxproject.model.Mentors
import com.example.ajaxproject.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

interface CategoriesRepository : CrudRepository<Categories, Int>