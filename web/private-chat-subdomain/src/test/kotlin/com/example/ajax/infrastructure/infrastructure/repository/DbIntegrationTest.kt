package com.example.ajax.infrastructure.infrastructure.repository

import com.example.ajax.infrastructure.infrastructure.repository.DbIntegrationTest.DbIntegrationTestComponentScan
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("it")
@EnableAutoConfiguration
@SpringBootTest
@ContextConfiguration(
    classes = [
        MongoRepositoriesAutoConfiguration::class,
        RedisAutoConfiguration::class,
        DbIntegrationTestComponentScan::class,
    ]
)
annotation class DbIntegrationTest {

    @ComponentScan(
        value = [
            "com.example.ajax.infrastructure",
        ]
    )
    class DbIntegrationTestComponentScan
}
