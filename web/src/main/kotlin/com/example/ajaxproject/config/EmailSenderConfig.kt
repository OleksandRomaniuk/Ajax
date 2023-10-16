package com.example.ajaxproject.config

import com.example.ajaxproject.service.EmailSenderServiceImpl
import com.example.ajaxproject.service.interfaces.EmailSenderService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class EmailSenderConfig {

    @Bean
    @Primary
    fun emailSender(javaMailSender: JavaMailSender): EmailSenderService {
        return EmailSenderServiceImpl(javaMailSender)
    }
}
