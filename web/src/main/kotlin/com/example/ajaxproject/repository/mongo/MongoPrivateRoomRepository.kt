package com.example.ajaxproject.repository.mongo

import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.model.mongo.MongoPrivateChatRoom
import com.example.ajaxproject.model.mongo.toDomain
import com.example.ajaxproject.model.toMongo
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MongoPrivateRoomRepository(private val mongoTemplate: ReactiveMongoTemplate) : PrivateChatRoomRepository {

    override fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom> {
        val query = Query().addCriteria(Criteria.where("id").`is`(chatRoomId))
        return mongoTemplate.findOne<MongoPrivateChatRoom>(query)
            .map { it.toDomain() }
    }

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        return mongoTemplate.save(privateChatRoom.toMongo())
            .map { it.toDomain() }
    }
}
