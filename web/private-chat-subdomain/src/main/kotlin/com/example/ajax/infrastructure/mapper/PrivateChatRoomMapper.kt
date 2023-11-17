package com.example.ajax.infrastructure.mapper

import com.example.ajax.infrastructure.configuration.mapper.EntityMapper
import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infrastructure.mongo.entity.PrivateChatRoomEntity
import com.example.ajax.infrastructure.entity.RedisPrivateChatRoom
import org.springframework.stereotype.Component

@Component
class PrivateChatRoomMapper : EntityMapper<PrivateChatRoom, PrivateChatRoomEntity> {

    override fun mapToDomain(entity: PrivateChatRoomEntity): PrivateChatRoom {
        return PrivateChatRoom(
            id = entity.id,
            senderId = entity.senderId,
            recipientId = entity.recipientId
        )
    }

    override fun mapToEntity(domain: PrivateChatRoom): PrivateChatRoomEntity {
        return PrivateChatRoomEntity(
            id = domain.id,
            senderId = domain.senderId,
            recipientId = domain.recipientId
        )
    }

    fun mapToRedis(domain: PrivateChatRoom): RedisPrivateChatRoom {
        return RedisPrivateChatRoom(
            id = domain.id,
            senderId = domain.senderId,
            recipientId = domain.recipientId
        )
    }

    fun fromRedisToDomain(redisPrivateChatRoom: RedisPrivateChatRoom): PrivateChatRoom {
        return PrivateChatRoom(
            id = redisPrivateChatRoom.id,
            senderId = redisPrivateChatRoom.senderId,
            recipientId = redisPrivateChatRoom.recipientId
        )
    }
}
