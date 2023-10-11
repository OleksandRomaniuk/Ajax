package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.UserResponse
import com.example.ajaxproject.dto.request.UserRequest
import com.example.ajaxproject.model.User
import com.example.ajaxproject.service.interfaces.UserService
import com.mongodb.client.result.DeleteResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
    fun createUser(@RequestBody userRequest: UserRequest): Mono<User> = userService.create(userRequest)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody userResponse: UserResponse): Mono<User> =
        userService.updateUser(userResponse.copy(id = id))

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): Mono<DeleteResult> = userService.deleteUser(id)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(
        @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(value = "size", defaultValue = "25", required = false) size: Int
    ): Flux<User> = userService.getAll(page, size)

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: String): Mono<User> = userService.getById(id)
}
