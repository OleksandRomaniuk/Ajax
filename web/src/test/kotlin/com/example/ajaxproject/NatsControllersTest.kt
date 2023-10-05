package com.example.ajaxproject

import com.example.ajaxproject.model.toProtoUser
import com.example.ajaxproject.repository.UserRepository
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.remove
import org.springframework.test.context.ActiveProfiles
import assertk.assertThat
import assertk.assertions.isEqualTo
import java.time.Duration

private const val  USER_ID = "651c6a8763d50fb3f7f1ec7b"
@SpringBootTest
class NatsControllersTest {

    @Autowired
    private lateinit var natsConnection: Connection

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var userRepository: UserRepository

    @AfterEach
    fun cleanDB() {
        mongoTemplate.remove<User>()
    }

    private val userToSave = com.example.ajaxproject.model.User(
        id = USER_ID,
        email = "email",
        password = "password",
    )
    private val protoUser = User.newBuilder().apply {
        setId(USER_ID)
        setEmail("email")
        setPassword("password")
    }.build()

    private fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3> doRequest(
        subject: String,
        payload: RequestT,
        parser: Parser<ResponseT>,
    ): ResponseT {
        val response = natsConnection.requestWithTimeout(
            subject,
            payload.toByteArray(),
            Duration.ofSeconds(10L)
        )
        return parser.parseFrom(response.get().data)
    }
}
