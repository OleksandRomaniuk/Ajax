package com.example.ajaxproject.email


import com.example.ajaxproject.pbp.LoggingBeanPostProcessor
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service("javaMailEmailSenderService")
internal class EmailSenderServiceImpl(private val javaMailSender: JavaMailSender) : EmailSenderService {

    override fun send(email: Email): SendEmailResult {

        LOGGER.info("Sending email")

        return try {
            javaMailSender.send(generateMailMessage(email))
            LOGGER.debug("Email sent successfully")
            SendEmailResult(status = 200)
        } catch (exception: MessagingException) {
            LOGGER.warn("An error has occurred sending email", exception)
            SendEmailResult(status = 500, cause = exception.message, exception = exception)
        } catch (exception: MailException) {
            LOGGER.warn("An error has occurred sending email", exception)
            SendEmailResult(status = 500, cause = exception.message, exception = exception)
        }
    }

    private fun generateMailMessage(email: Email): MimeMessage {
        val helper = MimeMessageHelper(javaMailSender.createMimeMessage())
        helper.setFrom(email.from)
        helper.setTo(email.to)
        helper.setSubject(email.subject!!)
        helper.setText(email.body, true)

        return helper.mimeMessage
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(LoggingBeanPostProcessor::class.java)
    }

}
