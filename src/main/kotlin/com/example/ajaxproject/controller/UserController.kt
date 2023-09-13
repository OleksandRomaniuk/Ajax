package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.model.User
import com.example.ajaxproject.service.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
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
        return ResponseEntity.ok(userService.createUser(userDTO))
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody userDTO: UserDTO): ResponseEntity<User> {
        return ResponseEntity.ok(userService.updateUser(id, userDTO))
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Unit> {
        return ResponseEntity.ok(userService.deleteUser(id))
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id: String): ResponseEntity<User> {
        return ResponseEntity.ok(userService.getUserById(id))
    }

    @GetMapping
    fun findAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }
}
