package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.repository.GroupChatMessageRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository


@Repository
class GroupChatMessageRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : GroupChatMessageRepository {

    override fun save(chatMessage: GroupChatMessage): GroupChatMessage = mongoTemplate.save(chatMessage)

    override fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage> {
        val query = Query.query(Criteria.where("groupChatRoomId").`is`(groupChatRoomId))
        return mongoTemplate.find(query, GroupChatMessage::class.java)
    }

    override fun findAllMessagesByDay(groupChatRoomId: String, pageable: Pageable): Page<DayMessages> {

        val aggregation = newAggregation(
            match(Criteria.where("groupChatRoom.id").`is`(groupChatRoomId)),
            project()
                .and("id").dateAsFormattedString("%Y-%m-%d").`as`("date")
                .and("message").`as`("message"),
            group("date")
                .count().`as`("messageCount")
                .addToSet("message").`as`("messages"),
            sort(Sort.by(Sort.Order.asc("_id"))),
            skip((pageable.pageNumber * pageable.pageSize).toLong()),
            limit(pageable.pageSize.toLong())
        )

        val aggregationResults = mongoTemplate.aggregate(aggregation, "group-chat-message", DayMessages::class.java)

        val mappedResults = aggregationResults.mappedResults

        val totalCount = mongoTemplate.count(Query.query(Criteria.where("groupChatRoom.id").`is`(groupChatRoomId)), GroupChatMessage::class.java)

        return PageImpl(mappedResults, pageable, totalCount)
    }
}
data class DayMessages(
    val date: String,
    val messageCount: Long,
    val messages: List<String>
)
