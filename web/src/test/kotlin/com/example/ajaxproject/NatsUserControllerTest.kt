package com.example.ajaxproject

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import com.example.ajaxproject.UserOuterClass.DeleteUserRequest
import com.example.ajaxproject.UserOuterClass.GetByIdUserRequest
import com.example.ajaxproject.UserOuterClass.CreateUserRequest
import com.example.ajaxproject.UserOuterClass.CreateUserResponse
import com.example.ajaxproject.UserOuterClass.GetByIdUserResponse
import com.example.ajaxproject.model.User
import com.example.ajaxproject.nats.UserMapper
import com.example.ajaxproject.repository.UserRepository
import io.nats.client.Connection
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.time.Duration

@SpringBootTest
class NatsUserControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun generateReplyForDeleteNatsRequest() {
        val user = User("id", "email", "email")
        userRepository.save(user).block()
        val sizeBeforeDeletion = userRepository.findAll(PageRequest.of(0, 25)).collectList().block()!!.size
        val request = DeleteUserRequest.newBuilder()
            .setUserId(user.id)
            .build()

        connection.requestWithTimeout(
            NatsSubject.DELETE_USER_BY_ID,
            request.toByteArray(),
            Duration.ofSeconds(10)
        ).get().data

        val sizeAfterDeletion = userRepository.findAll(PageRequest.of(0, 25)).collectList().block()!!.size
        assertThat(sizeBeforeDeletion).isGreaterThan(sizeAfterDeletion)
    }

    @Test
    fun generateReplyForCreateNatsRequest() {
        val user = User("id", "email", "email")
        val expected = CreateUserRequest.newBuilder()
            .setUser(userMapper.userToProto(user))
            .build()

        val actual = CreateUserResponse.parseFrom(
            connection
                .requestWithTimeout(
                    NatsSubject.ADD_USER,
                    expected.toByteArray(),
                    Duration.ofSeconds(10)
                )
                .get()
                .data
        )
        assertThat(actual.user).isEqualTo(expected.user)
    }

    @Test
    fun generateReplyForFindByIdNatsRequest() {
        val user = User("id", "email", "email")
        val expected = GetByIdUserRequest.newBuilder()
            .setUserId(user.id)
            .build()

        val actual = GetByIdUserResponse.parseFrom(
            connection
                .requestWithTimeout(
                    NatsSubject.GET_USER_BY_ID,
                    expected.toByteArray(),
                    Duration.ofSeconds(10)
                )
                .get()
                .data
        )
        assertThat(actual.user.id).isEqualTo(expected.userId)
    }
}
