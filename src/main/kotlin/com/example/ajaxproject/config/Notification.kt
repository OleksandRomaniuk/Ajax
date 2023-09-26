package com.example.ajaxproject.config

import com.example.ajaxproject.dto.request.GroupChatDTO
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Notification
