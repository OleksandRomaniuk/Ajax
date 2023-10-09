package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.repository.GroupChatMessageRepository
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.match
import org.springframework.data.mongodb.core.aggregation.Aggregation.sort
import org.springframework.data.mongodb.core.aggregation.Aggregation.skip
import org.springframework.data.mongodb.core.aggregation.Aggregation.limit
import org.springframework.data.mongodb.core.aggregation.Aggregation.count
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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

    override fun findMessagesByChatRoomIdWithPagination(
        chatRoomId: String,
        offset: Int,
        limit: Int
    ): Pair<List<GroupChatMessage>, Long> {

        val aggregation = newAggregation(
            match(Criteria.where("groupChatRoom").`is`(chatRoomId)),
            sort(Sort.by(Sort.Order.desc("date"))),
            skip(offset.toLong()),
            limit(limit.toLong()),
            count().`as`("totalCount")
        )

        val aggregationResults =
            mongoTemplate.aggregate(aggregation, "group-chat-message", GroupChatMessage::class.java)
        val messages = aggregationResults.mappedResults
        val totalCount = aggregationResults.uniqueMappedResult?: 0L

        return Pair(messages, totalCount as Long)
    }

}
