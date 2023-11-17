package com.example.ajax.infrastructure.mongo.repository

import com.example.ajax.application.port.PrivateChatRoomRepositoryOutPort
import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infrastructure.mapper.PrivateChatRoomMapper
import com.example.ajax.infrastructure.mongo.entity.PrivateChatRoomEntity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class MongoPrivateRoomRepository(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val privateChatRoomMapper: PrivateChatRoomMapper
) : PrivateChatRoomRepositoryOutPort {

    override fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom> {
        val query = Query().addCriteria(Criteria.where("id").`is`(chatRoomId))
        return mongoTemplate.findOne<PrivateChatRoomEntity>(query)
            .map { privateChatRoomMapper.mapToDomain(it) }
    }

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        return mongoTemplate.save(privateChatRoomMapper.mapToEntity(privateChatRoom))
            .map { privateChatRoomMapper.mapToDomain(it) }
    }
}
