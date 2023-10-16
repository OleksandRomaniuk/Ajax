package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.repository.GroupChatRoomRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

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

    override fun removeUserFromAllChats(userId: String) {
        val query = Query(Criteria.where("chatMembers.id").`is`(userId))
        val update = Update().pull("chatMembers", Query(Criteria.where("id").`is`(userId)))
        reactiveMongoTemplate.updateMulti(query, update, GroupChatRoom::class.java)
            .subscribe()
    }
}

