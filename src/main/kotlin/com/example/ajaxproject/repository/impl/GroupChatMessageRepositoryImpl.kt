package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.repository.GroupChatMessageRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.TypedAggregation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class GroupChatMessageRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
    private val objectMapper: ObjectMapper
) : GroupChatMessageRepository {

    override fun save(chatMessage: GroupChatMessage): GroupChatMessage = mongoTemplate.save(chatMessage)

    override fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage> {
        val query = Query.query(Criteria.where("groupChatRoomId").`is`(groupChatRoomId))
        return mongoTemplate.find(query, GroupChatMessage::class.java)
    }

    override fun getGroupChatMessagesByOffsetPagination(offset: Int, limit: Int): Pair<List<GroupChatMessage>, Long> {
        return getGroupChatMessagesAndTotalCountFromAggregationResult(
        TypedAggregation.newAggregation(
            GroupChatMessage::class.java,
            Aggregation.skip(offset.toLong()),
            Aggregation.limit(limit.toLong()),
            Aggregation.facet()
                .and(Aggregation.project(GroupChatMessage::class.java)).`as`("messages")
                .and(Aggregation.count().`as`("totalCount")).`as`("totalCount")
        ))
    }

    @Suppress("UNCHECKED_CAST")
    private fun getGroupChatMessagesAndTotalCountFromAggregationResult(
        aggregation: TypedAggregation<GroupChatMessage>
    ): Pair<List<GroupChatMessage>, Long> {
        val results = mongoTemplate.aggregate(aggregation, Map::class.java)

        val pagedMessages = results.getMappedResults().first()["messages"] as List<LinkedHashMap<*, *>>
        val messages = pagedMessages.map { messageMap ->
            val modifiedMessageMap = modifyObjectIdToHexId(messageMap)
            objectMapper.convertValue(modifiedMessageMap, GroupChatMessage::class.java)
        }

        val totalCountList = results.mappedResults.first()["totalCount"] as? List<LinkedHashMap<*, *>>
        val totalCount = ((totalCountList?.firstOrNull()?.get("totalCount") as? Int?)?.toLong()) ?: 0

        return Pair(messages, totalCount)
    }
    @Suppress("UNCHECKED_CAST")
    private fun modifyObjectIdToHexId(messageMap: LinkedHashMap<*, *>?): MutableMap<Any, Any>? {
        if (messageMap == null) {
            return null
        }

        val idValue = messageMap["_id"] as? ObjectId ?: return null
        val groupChatRoomValue = messageMap["groupChatRoom"] as? LinkedHashMap<*, *> ?: return null
        val groupChatRoomIdValue = groupChatRoomValue["_id"] as? ObjectId ?: return null

        val modifiedMessageMap = mutableMapOf<Any, Any>()
        modifiedMessageMap.putAll(messageMap)
        modifiedMessageMap.remove("_id")
        modifiedMessageMap["id"] = idValue.toHexString()

        val modifiedGroupChatRoom = mutableMapOf<Any, Any>()
        modifiedGroupChatRoom.putAll(groupChatRoomValue)
        modifiedGroupChatRoom.remove("_id")
        modifiedGroupChatRoom["id"] = groupChatRoomIdValue.toHexString()

        modifiedMessageMap["groupChatRoom"] = modifiedGroupChatRoom

        return modifiedMessageMap
    }
}
