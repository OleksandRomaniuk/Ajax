package com.example.ajaxproject

object UserEvent {

    private const val SERVICE_NAME = "chat"

    private const val SUBDOMAIN = "user"

    const val DELETE = "$SERVICE_NAME.output.pubsub.$SUBDOMAIN.delete"

    const val UPDATED = "updated"

    fun createUserEventKafkaTopic(eventType: String): String =
        "$SERVICE_NAME.$SUBDOMAIN.$eventType"

    fun createUserEventNatsSubject(userId: String, eventType: String): String =
        "$SERVICE_NAME.$SUBDOMAIN.$userId.$eventType"
}
