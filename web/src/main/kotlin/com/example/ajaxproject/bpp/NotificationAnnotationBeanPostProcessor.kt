package com.example.ajaxproject.bpp

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
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux
import java.lang.reflect.Method
import java.util.concurrent.ExecutorService
import kotlin.reflect.KClass

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
class NotificationAnnotationBeanPostProcessor(
    private val beanFactory: BeanFactory
) : BeanPostProcessor {

    private val serviceBeans = mutableMapOf<String, KClass<*>>()
    private val beanAnnotatedMethods = mutableMapOf<String, List<Method>>()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean::class
        if (beanClass.java.isAnnotationPresent(Service::class.java)) {
            val annotatedMethods = findAnnotatedMethods(beanClass.java)
            if (annotatedMethods.isNotEmpty()) {
                serviceBeans[beanName] = beanClass
                beanAnnotatedMethods[beanName] = annotatedMethods
            }
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        return if (serviceBeans.containsKey(beanName)) {
            Proxy.newProxyInstance(
                bean::class.java.classLoader,
                bean::class.java.interfaces,
                NotificationInvocationHandler(bean, beanFactory, beanAnnotatedMethods[beanName] ?: emptyList())
            )
        } else {
            bean
        }
    }

    private fun findAnnotatedMethods(beanClass: Class<*>): List<Method> {
        return beanClass.methods.filter { it.isAnnotationPresent(Notification::class.java) }
    }
}


@Suppress("SpreadOperator")
class NotificationInvocationHandler(
    private val bean: Any,
    private val beanFactory: BeanFactory,
    private val annotatedMethods: List<Method>
) : InvocationHandler {
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        val result = method.invoke(bean, *args.orEmpty())

        for (annotatedMethod in annotatedMethods) {
            if (method.name == annotatedMethod.name) {
                val identifiableArg = args?.find { it is Identifiable } as? Identifiable
                if (identifiableArg != null) {
                    createNotification(identifiableArg)
                }
            }
        }
        return result
    }

    private fun createNotification(identifiable: Identifiable) {
        val groupChatRoomRepository = beanFactory.getBean(GroupChatRoomRepository::class.java)
        val emailSenderService = beanFactory.getBean(EmailSenderService::class.java)
        val sendEmailThreadPool = beanFactory.getBean("sendEmailThreadPool", ExecutorService::class.java)

        val chatMono = groupChatRoomRepository.findChatRoom(identifiable.chatId).cache()

        chatMono
            .flatMapMany { it.chatMembers.toFlux() }
            .filter { user -> emailSenderService.isValidEmail(user.email) }
            .flatMap { user ->
                chatMono.map { chat -> buildEmail(user.email, chat.chatName) }
                    .flatMap { email -> emailSenderService.send(email) }
            }
            .doOnComplete { logger.info("Emails sent to all valid users.") }
            .subscribeOn(Schedulers.fromExecutor(sendEmailThreadPool))
            .subscribe()
    }


    private fun buildEmail(email: String, chatName: String): EmailDTO = EmailDTO(
        from = "ora.romaniuk@gmail.com",
        to = email,
        subject = "Testing post bean processor",
        body = "New message in chat $chatName"
    )

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationAnnotationBeanPostProcessor::class.java)
    }
}
