package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.domain.GroupChatRoom
import com.example.ajaxproject.repository.GroupChatRoomRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Repository
class GroupChatRoomRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : GroupChatRoomRepository {

    override fun findChatRoom(chatId: String): Mono<GroupChatRoom> {
        val chatQuery = Query.query(Criteria.where("_id").`is`(chatId))
        return reactiveMongoTemplate.findOne(chatQuery, GroupChatRoom::class.java)
    }

    override fun save(chat: GroupChatRoom): Mono<GroupChatRoom> {
        return reactiveMongoTemplate.save(chat)
    }

    override fun removeUserFromAllChats(userId: String): Mono<Unit> {
        val query = Query(Criteria.where("chatMembers.id").`is`(userId))
        val update = Update().pull("chatMembers", Query(Criteria.where("id").`is`(userId)))
        return reactiveMongoTemplate.updateMulti(query, update, GroupChatRoom::class.java)
            .then(Unit.toMono())
    }
}

