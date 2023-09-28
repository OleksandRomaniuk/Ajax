package com.example.ajaxproject.pbp

import com.example.ajaxproject.config.Notification
import com.example.ajaxproject.dto.request.EmailDTO
import com.example.ajaxproject.dto.request.Identifiable
import com.example.ajaxproject.repository.GroupChatRoomRepository
import com.example.ajaxproject.service.interfaces.EmailSenderService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.cglib.proxy.Proxy
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.lang.reflect.Method
import java.util.concurrent.ExecutorService
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

@Component
class NotificationAnnotationBeanPostProcessor(
    private val beanFactory: BeanFactory
) : BeanPostProcessor {

    private val serviceBeans = mutableMapOf<String, KClass<*>>()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {

        val beanClass = bean::class
        if (beanClass.java.isAnnotationPresent(Service::class.java)) {
            val hasNotificationAnnotation = beanClass.memberFunctions.any { beanMethod ->
                beanMethod.annotations.any { it is Notification }
            }
            if (hasNotificationAnnotation) {
                serviceBeans[beanName] = beanClass
            }
        }
        return super.postProcessBeforeInitialization(bean, beanName)
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        return if (serviceBeans.containsKey(beanName)) {
            Proxy.newProxyInstance(
                bean::class.java.classLoader,
                bean::class.java.interfaces,
                NotificationInvocationHandler(bean, beanFactory)
            )
        } else {
            bean
        }
    }
}

class NotificationInvocationHandler(
    private val bean: Any,
    private val beanFactory: BeanFactory
) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        val result = method.invoke(bean, *args.orEmpty())

        if (args?.any { it is Identifiable } == true) {
            createNotification(args.find { it is Identifiable } as Identifiable)
        }
        return result
    }

    private fun createNotification(identifiable: Identifiable) {

        val groupChatRoomRepository = beanFactory.getBean(GroupChatRoomRepository::class.java)
        val chat = groupChatRoomRepository.findChatRoom(identifiable.chatId)
        val chatName = chat.chatName
        val userList = chat.chatMembers

        val emailSenderService = beanFactory.getBean(EmailSenderService::class.java)

        val executorService = beanFactory.getBean("sendEmailThreadPool", ExecutorService::class.java)

        userList.asSequence()
            .map { user -> buildEmail(user.email, chatName) }
            .forEach { email ->
                executorService.submit {
                    emailSenderService.send(email)
                }
            }

        logger.info("Emails sent to users: {}", userList)
    }
    private fun buildEmail(email: String , chatName: String): EmailDTO = EmailDTO(
        from = "ora.romaniuk@gmail.com",
        to = email,
        subject = "Testing post bean processor",
        body = "New message in chat $chatName"
    )
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationAnnotationBeanPostProcessor::class.java)
    }
}
