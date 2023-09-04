package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.CategoryPriceDTO
import com.example.ajaxproject.dto.MentorUpdateDTO
import com.example.ajaxproject.model.Mentors
import com.example.ajaxproject.model.User

interface MentorsService {

    fun updateMentorInformation(id: Long, mentorUpdateDTO: MentorUpdateDTO): Mentors

    fun addCategoriesAndPrices(id: Long, categoryPriceDTOList: List<CategoryPriceDTO>)

    fun getAllMentors(): List<Mentors>

    fun findAllMentors(): List<User>

}
