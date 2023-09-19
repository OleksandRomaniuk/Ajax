package com.example.ajaxproject.pbp

import com.example.ajaxproject.config.Notification
import com.example.ajaxproject.dto.request.GroupChatDTO
import com.example.ajaxproject.email.Email
import com.example.ajaxproject.email.EmailSenderService
import com.example.ajaxproject.repository.GroupChatRoomRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.cglib.proxy.Proxy
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

@Component
class NotificationAnnotationBeanPostProcessor(
    @Qualifier("javaMailEmailSenderService") private val emailSenderService: EmailSenderService
) : BeanPostProcessor {

    @Autowired
    private lateinit var groupChatRoomRepository: GroupChatRoomRepository

    private val beans = mutableMapOf<String, KClass<*>>()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean::class

        if (beanClass.java.isAnnotationPresent(Service::class.java)) {
            beans[beanName] = beanClass
        }

        return super.postProcessBeforeInitialization(bean, beanName)
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        return beans[beanName]?.let { beanClass ->
            Proxy.newProxyInstance(
                beanClass.java.classLoader,
                beanClass.java.interfaces,
                DeviceAuthorizationInvocationHandler(bean, groupChatRoomRepository, emailSenderService,beanClass)
            )
        } ?: bean
    }
}

@Suppress("SpreadOperator")
class DeviceAuthorizationInvocationHandler(
    private val bean: Any,
    private val groupChatRoomRepository: GroupChatRoomRepository,
    private val emailSenderService: EmailSenderService,
    private val originalBean: KClass<*>

) : InvocationHandler {

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        val methodParams = args ?: emptyArray()
        val result = method.invoke(bean, *methodParams)

        if (args?.any { it is  GroupChatDTO} == true && hasNotificationAnnotation(originalBean, method)) {
            val userRequest = args.find { it is GroupChatDTO } as GroupChatDTO
            createNotification(userRequest)
        }

        return result
    }

    private fun hasNotificationAnnotation(originalBean: KClass<*>, method: Method): Boolean {
        return originalBean.memberFunctions.any { beanMethod ->
            beanMethod.name == method.name &&
                    beanMethod.javaClass.typeParameters.contentEquals(method.javaClass.typeParameters) &&
                    beanMethod.findAnnotation<Notification>() != null
        }
    }

    private fun createNotification(groupChatDTO: GroupChatDTO) {

        val chatName = groupChatRoomRepository.findChatRoom(groupChatDTO.chatId).chatName

        val userList = groupChatRoomRepository.findById(ObjectId(groupChatDTO.chatId)).get().chatMembers

        logger.info("Emails sent to users: {}", userList)

        for (user in userList) {
            val email = Email(
                from = "ora.romaniuk@gmail.com",
                to = user.email,
                subject = "Testing post bean processor",
                body = "New message in chat $chatName"
            )
           emailSenderService.send(email)
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationAnnotationBeanPostProcessor::class.java)
    }
}
