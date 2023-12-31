package com.example.ajax.infrastructure.bpp

import com.example.ajax.infrastructure.dto.Identifiable
import com.example.ajax.infrastructure.email.EmailSenderServiceOutPort
import com.example.ajax.infrastructure.email.dto.EmailRequest
import com.example.ajax.application.port.GroupChatRoomRepositoryOutPort
import com.example.ajax.application.port.Notification
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
                NotificationInvocationHandler(bean, beanFactory, beanAnnotatedMethods[beanName].orEmpty())
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
        val groupChatRoomRepositoryOutPort = beanFactory.getBean(GroupChatRoomRepositoryOutPort::class.java)
        val emailSenderServiceOutPort = beanFactory.getBean(EmailSenderServiceOutPort::class.java)
        val sendEmailThreadPool = beanFactory.getBean("sendEmailThreadPool", ExecutorService::class.java)

        val chatMono = groupChatRoomRepositoryOutPort.findChatRoom(identifiable.chatRoomId).cache()

        chatMono
            .flatMapMany { it.chatMembers.toFlux() }
            .filter { user -> emailSenderServiceOutPort.isValidEmail(user.email) }
            .flatMap { user ->
                chatMono.map { chat -> buildEmail(user.email, chat.chatName) }
                    .flatMap { email -> emailSenderServiceOutPort.send(email) }
            }
            .doOnComplete { logger.info("Emails sent to all valid users.") }
            .subscribeOn(Schedulers.fromExecutor(sendEmailThreadPool))
            .subscribe()
    }

    private fun buildEmail(email: String, chatName: String): EmailRequest = EmailRequest(
        from = "ora.romaniuk@gmail.com",
        to = email,
        subject = "Testing post bean processor",
        body = "New message in chat $chatName"
    )

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationAnnotationBeanPostProcessor::class.java)
    }
}
