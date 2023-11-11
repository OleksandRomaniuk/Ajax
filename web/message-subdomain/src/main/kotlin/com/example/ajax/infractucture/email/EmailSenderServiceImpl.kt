package com.example.ajax.infractucture.email

import com.example.ajax.infractucture.email.dto.EmailRequest
import com.example.ajax.infractucture.email.dto.EmailResponse
import jakarta.mail.internet.MimeMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
internal class EmailSenderServiceImpl(
    private val javaMailSender: JavaMailSender
) : EmailSenderService {

    override fun send(emailRequest: EmailRequest): Mono<EmailResponse> {
        return Mono.fromCallable {
            javaMailSender.send(generateMailMessage(emailRequest))
        }
            .doOnSuccess { logger.debug("Email sent successfully") }
            .thenReturn(EmailResponse(status = 200))
            .onErrorResume { error ->
                logger.error("An unexpected error has occurred sending email", error)
                Mono.just(EmailResponse(status = 500, cause = error.message))
            }
            .subscribeOn(Schedulers.boundedElastic())
    }

    private fun generateMailMessage(emailRequest: EmailRequest): MimeMessage {
        return MimeMessageHelper(javaMailSender.createMimeMessage()).apply {
            setFrom(emailRequest.from)
            setTo(emailRequest.to)
            setSubject(emailRequest.subject)
            setText(emailRequest.body, true)
        }.mimeMessage
    }

    override fun isValidEmail(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EmailSenderServiceImpl::class.java)
        private val EMAIL_REGEX: Regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
    }
}
