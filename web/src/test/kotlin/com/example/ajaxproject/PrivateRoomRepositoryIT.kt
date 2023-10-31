package com.example.ajaxproject

import com.example.ajaxproject.UserOuterClass.User
import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.model.mongo.MongoPrivateChatRoom
import com.example.ajaxproject.model.mongo.toDomain
import com.example.ajaxproject.repository.cacheable.CacheableRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.test.StepVerifier

@DbIntegrationTest
@SpringBootTest
class PrivateRoomRepositoryIT {

    @Autowired
    private lateinit var cacheableRepository: CacheableRepository<PrivateChatRoom>

    @Autowired
    private lateinit var mongoTemplate: ReactiveMongoTemplate

    @Autowired
    private lateinit var redisTemplate: ReactiveRedisTemplate<String, PrivateChatRoom>

    @Test
    fun `should create private chat room`() {
        // GIVEN
        val roomId = "room-id-${System.nanoTime()}"
        val newRoom = PrivateChatRoom(id = roomId, senderId = "senderId", recipientId = "recipientId")

        // WHEN
        val resultMono = cacheableRepository.save(newRoom)

        // THEN
        StepVerifier.create(resultMono)
            .expectNext(newRoom)
            .verifyComplete()

        // AND THEN
        val actualFromDb = mongoTemplate.findById(roomId, MongoPrivateChatRoom::class.java).block()!!.toDomain()

        Assertions.assertEquals(roomId, actualFromDb.id)
        Assertions.assertEquals(newRoom.senderId, actualFromDb.senderId)
        Assertions.assertEquals(newRoom.recipientId, actualFromDb.recipientId)
    }

    @Test
    fun `should retrieve private chat room by id from Redis`() {
        // GIVEN
        val roomId = "room-id-${System.nanoTime()}"
        val newRoom = PrivateChatRoom(id = roomId, senderId = "senderId", recipientId = "recipientId")

        // WHEN
        cacheableRepository.save(newRoom).block()

        // THEN
        val cachedRoom = redisTemplate.opsForValue().get(roomId).block()

        Assertions.assertNotNull(cachedRoom)
        Assertions.assertEquals(newRoom, cachedRoom)
    }
}
