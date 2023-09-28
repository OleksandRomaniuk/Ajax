package com.example.ajaxproject.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class ThreadPoolConfig {

    @Bean("sendEmailThreadPool", destroyMethod = "shutdown")
    fun sendEmailThreadPool(): ExecutorService {
        return Executors.newFixedThreadPool(4)
    }
}
