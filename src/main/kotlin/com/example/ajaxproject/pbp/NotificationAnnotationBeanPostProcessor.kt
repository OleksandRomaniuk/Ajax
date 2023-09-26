package com.example.ajaxproject.pbp

import com.example.ajaxproject.config.Notification
import com.example.ajaxproject.dto.request.ChatDTO
import com.example.ajaxproject.dto.request.EmailDTO
import com.example.ajaxproject.repository.GroupChatRoomRepository
import com.example.ajaxproject.service.interfaces.EmailSenderService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.cglib.proxy.Proxy
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.lang.reflect.Method
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

        if (args?.any { it is ChatDTO } == true) {
            createNotification(args.find { it is ChatDTO } as ChatDTO)
        }
        return result
    }

    private fun createNotification(chatDTO: ChatDTO) = runBlocking {
        val groupChatRoomRepository = beanFactory.getBean(GroupChatRoomRepository::class.java)
        val chatName = groupChatRoomRepository.findChatRoom(chatDTO.chatId).chatName
        val userList = groupChatRoomRepository.findChatRoom(chatDTO.chatId).chatMembers

        val emailSenderService = beanFactory.getBean(EmailSenderService::class.java)

        val deferredEmails = userList.map { user ->
            CoroutineScope(Dispatchers.Default).async {
                val emailDTO = EmailDTO(
                    from = "ora.romaniuk@gmail.com",
                    to = user.email,
                    subject = "Testing post bean processor",
                    body = "New message in chat $chatName"
                )
                emailSenderService.send(emailDTO)
            }
        }

        deferredEmails.forEach { it.await() }

        logger.info("Emails sent to users: {}", userList)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationAnnotationBeanPostProcessor::class.java)
    }
}
