package com.example.ajax.infrastructure.mapper

import com.example.ajax.infrastructure.configuration.mapper.EntityMapper
import com.example.ajax.domain.GroupChatRoom
import com.example.ajax.infrastructure.mongo.entity.GroupChatRoomEntity
import org.springframework.stereotype.Component

@Component
class GroupChatRoomMapper : EntityMapper<GroupChatRoom, GroupChatRoomEntity> {

    override fun mapToDomain(entity: GroupChatRoomEntity): GroupChatRoom {
        return GroupChatRoom(
            id = entity.id,
            adminId = entity.adminId,
            chatName = entity.chatName,
            chatMembers = entity.chatMembers
        )
    }

    override fun mapToEntity(domain: GroupChatRoom): GroupChatRoomEntity {
        return GroupChatRoomEntity(
            id = domain.id,
            adminId = domain.adminId,
            chatName = domain.chatName,
            chatMembers = domain.chatMembers
        )
    }
}
