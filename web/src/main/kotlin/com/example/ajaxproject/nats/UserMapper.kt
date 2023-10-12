package com.example.ajaxproject.nats

import com.example.ajaxproject.UserOuterClass
import com.example.ajaxproject.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun userToProto(user: User): UserOuterClass.User {
        return UserOuterClass.User.newBuilder().apply {
            id = user.id
            email = user.email
            password = user.password
        }.build()
    }

    fun protoToUser(userProto: UserOuterClass.User): User {
        return User(
            id = userProto.id,
            email = userProto.email,
            password = userProto.password,
        )
    }

    fun userToProtoResponse(user: User): UserOuterClass.CreateUserResponse {
        return UserOuterClass.CreateUserResponse.newBuilder()
            .setUser(userToProto(user))
            .build()
    }

    fun protoRequestToUser(userProto: UserOuterClass.CreateUserRequest): User {
        return User(
            id = userProto.user.id,
            email = userProto.user.email,
            password = userProto.user.password,
        )
    }
}
