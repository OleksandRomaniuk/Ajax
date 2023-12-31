package com.example.ajax.infrastructure.infrastructure.repository

import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infrastructure.cacheable.CacheablePrivateRoomRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.test.StepVerifier

@DbIntegrationTest
@SpringBootTest
class PrivateRoomRepositoryIT {

    @Autowired
    private lateinit var cacheableRepository: CacheablePrivateRoomRepository

    @Autowired
    private lateinit var redisTemplate: ReactiveRedisTemplate<String, PrivateChatRoom>

    @Test
    fun `should create private chat room and cache on save`() {

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
        val actualFromDb = cacheableRepository.findPrivateChatRoomById(roomId).block()!!
        Assertions.assertEquals(roomId, actualFromDb.id)
        Assertions.assertEquals(newRoom.senderId, actualFromDb.senderId)
        Assertions.assertEquals(newRoom.recipientId, actualFromDb.recipientId)

        // AND THEN
        val cachedRoom = redisTemplate.opsForValue().get(roomId).block()
        Assertions.assertNotNull(cachedRoom)
        Assertions.assertEquals(newRoom, cachedRoom)
    }
}
