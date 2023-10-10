package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.model.User
import com.example.ajaxproject.service.interfaces.UserService
import com.mongodb.client.result.DeleteResult
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
    private val userService: UserService,
) {

    @PostMapping("/create")
    fun createUser(@RequestBody userDTO: UserDTO): ResponseEntity<Mono<User>> {
        val userMono = userService.create(userDTO)
        return ResponseEntity.ok(userMono)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody userDTO: UserDTO): ResponseEntity<Mono<User>> {
        val updatedUserMono = userService.updateUser(userDTO.copy(id = id))
        return ResponseEntity.ok(updatedUserMono)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Mono<DeleteResult>> {
        val deletedUserMono = userService.deleteUser(id)
        return ResponseEntity.ok(deletedUserMono)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): Flux<User> = userService.getAll(page, size)

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: String): Mono<User> =
        userService.getById(id)
}
