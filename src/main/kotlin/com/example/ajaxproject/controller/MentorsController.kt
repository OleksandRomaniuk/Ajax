package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.CategoryPriceDTO
import com.example.ajaxproject.dto.MentorDTO
import com.example.ajaxproject.dto.MentorUpdateDTO
import com.example.ajaxproject.model.Mentors
import com.example.ajaxproject.model.User
import com.example.ajaxproject.model.enums.Role
import com.example.ajaxproject.model.enums.Role.MENTOR
import com.example.ajaxproject.service.interfaces.MentorsService
import com.example.ajaxproject.service.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*



@RestController
@RequestMapping("/api/mentors")
class MentorsController @Autowired constructor(
    private val mentorsService: MentorsService,
    private val userService: UserService,
) {

   @PutMapping("/{id}")
    fun updateMentorInformation(@PathVariable id: Long, @RequestBody mentorUpdateDTO: MentorUpdateDTO
    ): ResponseEntity<Mentors> {
        val updatedMentor = mentorsService.updateMentorInformation(id, mentorUpdateDTO)
        return ResponseEntity.ok(updatedMentor)
    }

    @PostMapping("/{id}/categories")
    fun addCategoriesAndPrices(@PathVariable id: Long, @RequestBody categoryPriceDTOList: List<CategoryPriceDTO>
    ): ResponseEntity<Unit> {
        mentorsService.addCategoriesAndPrices(id, categoryPriceDTOList)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping
    fun getAllMentors(): List<User> {
        return userService.findAllUsersByRole(Role.MENTOR)
    }

}
