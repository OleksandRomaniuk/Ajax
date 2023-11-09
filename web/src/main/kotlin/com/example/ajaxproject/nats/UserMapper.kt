package com.example.ajaxproject.nats

import com.example.ajax.User
import com.pubsub.user.UserUpdatedEvent
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun userToProto(user: com.example.ajaxproject.domain.User): User {
        return User.newBuilder().apply {
            id = user.id
            email = user.email
            password = user.password
        }.build()
    }

    fun protoToUser(userProto: User): com.example.ajaxproject.domain.User {
        return com.example.ajaxproject.domain.User(
            id = userProto.id,
            email = userProto.email,
            password = userProto.password,
        )
    }
}

fun User.mapToUserUpdatedEvent(): UserUpdatedEvent =
    UserUpdatedEvent.newBuilder()
        .setUser(this)
        .build()
