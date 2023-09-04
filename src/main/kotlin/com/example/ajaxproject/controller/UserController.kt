package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.UserDTO
import com.example.ajaxproject.model.User
import com.example.ajaxproject.service.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
    private val userService: UserServiceImpl
) {

    @PostMapping("/create")
    fun createUser(@RequestBody userDTO: UserDTO): ResponseEntity<User> {
        val createdUser = userService.createUser(userDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userDTO: UserDTO): ResponseEntity<User> {
        val updatedUser = userService.updateUser(id, userDTO)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: Long): ResponseEntity<User?> {
        val user = userService.findUserById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping
    fun findAllUsers(): ResponseEntity<List<User>> {
        val users = userService.findAllUsers()
        return ResponseEntity.ok(users)
    }
}
