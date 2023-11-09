package com.example.ajax.infractucture.rest

import com.example.ajax.application.service.UserService
import com.example.ajax.domain.User
import com.example.ajax.infractucture.mongo.mapper.UserRequest
import com.example.ajax.infractucture.mongo.mapper.UserResponse
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
    fun createUser(@RequestBody user: User): Mono<User> = userService.create(user)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody user: User): Mono<User> =
        userService.updateUser(user)

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): Mono<User> = userService.deleteUser(id)

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
