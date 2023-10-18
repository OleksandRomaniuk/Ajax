package com.example.ajaxproject

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import com.example.ajaxproject.UserOuterClass.*
import com.example.ajaxproject.model.User
import com.example.ajaxproject.nats.UserMapper
import com.example.ajaxproject.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.nats.client.Connection
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.time.Duration
import io.nats.client.Message

@SpringBootTest
class NatsUserControllerTest {

    @Autowired
    lateinit var connection: Connection

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `should return success result`() {
        // GIVEN
        val user = User("id", "email", "password")
        val expected = CreateUserRequest.newBuilder()
            .setUser(userMapper.userToProto(user))
            .build()
        // WHEN
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
        // THEN
        assertThat(actual.user).isEqualTo(expected.user)
    }

    @Test
    fun `should return success result for deleting user`() {
        // GIVEN
        val user = User("id", "email", "password")
        userRepository.save(user).block()
        val sizeBeforeDeletion = userRepository.findAll(PageRequest.of(0, 25)).collectList().block()!!.size
        val request = UserOuterClass.DeleteUserRequest.newBuilder()
            .setUserId(user.id)
            .build()
        // WHEN
        connection.requestWithTimeout(
            NatsSubject.DELETE_USER_BY_ID,
            request.toByteArray(),
            Duration.ofSeconds(10)
        ).get().data
        // THEN
        val sizeAfterDeletion = userRepository.findAll(PageRequest.of(0, 25)).collectList().block()!!.size
        assertThat(sizeBeforeDeletion).isGreaterThan(sizeAfterDeletion)
    }


}







