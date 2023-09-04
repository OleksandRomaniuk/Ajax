package com.example.ajaxproject.service

import com.example.ajaxproject.dto.CategoryPriceDTO
import com.example.ajaxproject.dto.MentorDTO
import com.example.ajaxproject.dto.MentorUpdateDTO
import com.example.ajaxproject.model.Categories
import com.example.ajaxproject.model.MentorToCategories
import com.example.ajaxproject.model.Mentors
import com.example.ajaxproject.model.User
import com.example.ajaxproject.model.enums.Role
import com.example.ajaxproject.repository.MentorToCategoriesRepository
import com.example.ajaxproject.repository.MentorsRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.MentorsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class MentorsServiceImpl @Autowired constructor(
    private val mentorsRepository: MentorsRepository,
    private val userRepository: UserRepository,
    private val mentorToCategoriesRepository: MentorToCategoriesRepository,
) : MentorsService {

    override fun updateMentorInformation(id: Long, mentorUpdateDTO: MentorUpdateDTO): Mentors {
        val mentor = mentorsRepository.findById(id)
            .orElseThrow { NoSuchElementException("Mentor not found") }

        mentor.updateFromDTO(mentorUpdateDTO)

        return mentorsRepository.save(mentor)
    }

    override fun addCategoriesAndPrices(id: Long, categoryPriceDTOList: List<CategoryPriceDTO>) {
        val mentor = mentorsRepository.findById(id)
            .orElseThrow { NoSuchElementException("Mentor not found") }

        for (categoryPriceDTO in categoryPriceDTOList) {
            val category = Categories(id = categoryPriceDTO.categoryId)
            val mentorToCategory = MentorToCategories(
                price = categoryPriceDTO.price,
                course = category,
                mentors = mentor
            )
            mentorToCategoriesRepository.save(mentorToCategory)
        }
    }
    override fun getAllMentors(): List<Mentors> {
        return mentorsRepository.findAll()
    }

    override fun findAllMentors(): List<User> {
        return userRepository.findUserByRole(Role.MENTOR)
    }

}
