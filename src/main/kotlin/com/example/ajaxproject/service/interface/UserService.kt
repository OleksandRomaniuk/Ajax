package com.example.ajaxproject.service.`interface`

import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
    val userRepository: UserRepository
) : UserServiceInterface {

    override fun findAll(): List<User> = userRepository.findAll().toList()

    override fun findById(id: Long): User =
        userRepository.findById(id).orElseThrow { ChangeSetPersister.NotFoundException() }

    override fun create(entity: User): User = userRepository.save(entity)

    override fun delete(id: Long) = userRepository.deleteById(id)

    override fun update(id: Long, entity: User) {
        entity.id = findById(id).id
        create(entity)
    }

}