package com.example.ajax.infractucture.mongo.mapper

import com.example.ajax.User as ProtoUser
import com.example.ajax.domain.User
import com.example.ajax.infractucture.mongo.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper : EntityMapper<User, UserEntity , ProtoUser>  {

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

    override fun mapToProto(user: User): ProtoUser {
        return ProtoUser.newBuilder()
            .setId(user.id)
            .setEmail(user.email)
            .setPassword(user.password)
            .build()
    }
}
