package com.example.ajax.infrastructure.email

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.mail.javamail.JavaMailSender


@Configuration
class EmailSenderConfig {

    @Bean
    @Primary
    fun emailSender(javaMailSender: JavaMailSender): EmailSenderServiceOutPort {
        return EmailSenderService(javaMailSender)
    }
}
