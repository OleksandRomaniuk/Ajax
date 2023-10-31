package com.example.ajaxproject

import com.example.ajaxproject.MessageDestinations.EVENT_PREFIX

object UserEvent {
    private const val USER_PREFIX = "$EVENT_PREFIX.user"

    const val DELETE = "delete"

    fun createUserEventKafkaTopic(eventType: String): String = "$USER_PREFIX.$eventType"
}
