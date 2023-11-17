package com.example.ajax.infrastructure.mapper

import com.example.ajax.infrastructure.configuration.mapper.EntityMapper
import com.example.ajax.domain.User
import com.example.ajax.infrastructure.mongo.entity.UserEntity
import com.pubsub.user.UserUpdatedEvent
import org.springframework.stereotype.Component
import com.example.ajax.User as ProtoUser

@Component
class UserMapper : EntityMapper<User, UserEntity> {

    override fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.userId,
            email = entity.email,
            password = entity.password,
        )
    }

    override fun mapToEntity(domain: User): UserEntity {
        return UserEntity(
            userId = domain.id,
            email = domain.email,
            password = domain.password,
        )
    }

    fun mapToProto(entity: User): ProtoUser {
        return ProtoUser.newBuilder()
            .setId(entity.id)
            .setEmail(entity.email)
            .setPassword(entity.password)
            .build()
    }

    fun protoToUser(userProto: ProtoUser): User {
        return User(
            id = userProto.id,
            email = userProto.email,
            password = userProto.password,
        )
    }

    fun mapToUserUpdatedEvent(user: ProtoUser): UserUpdatedEvent =
        UserUpdatedEvent.newBuilder()
            .setUser(user)
            .build()
}
