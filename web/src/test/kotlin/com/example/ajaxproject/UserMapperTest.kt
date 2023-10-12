package com.example.ajaxproject;

import com.example.ajaxproject.model.User
import com.example.ajaxproject.nats.UserMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserMapperTest {

    private lateinit var userMapper: UserMapper

    @BeforeEach
    fun setUp() {
        userMapper = UserMapper()
    }

    @Test
    fun testUserToProto() {
        val user = User("1", "test@example.com", "password")
        val userProto = userMapper.userToProto(user)
        assert(userProto.id == user.id)
        assert(userProto.email == user.email)
        assert(userProto.password == user.password)
    }

    @Test
    fun testProtoToUser() {
        val userProto = UserOuterClass.User.newBuilder()
            .setId("1")
            .setEmail("test@example.com")
            .setPassword("password")
            .build()
        val user = userMapper.protoToUser(userProto)

        assert(user.id == userProto.id)
        assert(user.email == userProto.email)
        assert(user.password == userProto.password)
    }

    @Test
    fun testUserToProtoResponse() {
        val user = User("1", "test@example.com", "password")
        val userResponse = userMapper.userToProtoResponse(user)
        val userProto = userMapper.userToProto(user)
        assert(userResponse.user == userProto)
    }

    @Test
    fun testProtoRequestToUser() {
        val userProtoRequest = UserOuterClass.CreateUserRequest.newBuilder()
            .setUser(
                UserOuterClass.User.newBuilder()
                    .setId("1")
                    .setEmail("test@example.com")
                    .setPassword("password")
            )
            .build()
        val user = userMapper.protoRequestToUser(userProtoRequest)
        val userProto = userProtoRequest.user
        assert(user.id == userProto.id)
        assert(user.email == userProto.email)
        assert(user.password == userProto.password)
    }
}
