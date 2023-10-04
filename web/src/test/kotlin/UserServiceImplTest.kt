//import com.example.ajaxproject.dto.request.UserDTO
//import com.example.ajaxproject.model.User
//import com.example.ajaxproject.repository.UserRepository
//import com.example.ajaxproject.service.UserServiceImpl
//import com.example.ajaxproject.service.mapper.UserMapper
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertNotNull
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.mockito.InjectMocks
//import org.mockito.Mock
//import org.mockito.Mockito.`when`
//import org.mockito.Mockito.`verify`
//import org.mockito.Mockito.`times`
//
//import org.mockito.MockitoAnnotations
//
//class UserServiceImplTest {
//
//    @Mock
//    private lateinit var userRepository: UserRepository
//
//    @Mock
//    private lateinit var userMapper: UserMapper
//
//    @InjectMocks
//    private lateinit var userService: UserServiceImpl
//
//    @BeforeEach
//    fun setUp() {
//        MockitoAnnotations.initMocks(this)
//    }
//
//    @Test
//    fun testCreateUser() {
//        val userDTO = UserDTO()
//        userDTO.email = "test@example.com"
//        userDTO.password = "password"
//
//        val user = User(
//            id = "1",
//            email = "test@example.com",
//            password = "password"
//        )
//
//        `when`(userMapper.toEntity(userDTO)).thenReturn(user)
//        `when`(userRepository.save(user)).thenReturn(user)
//
//        val createdUser = userService.createUser(userDTO)
//
//        verify(userRepository, times(1)).save(user)
//
//        assertNotNull(createdUser)
//        assertEquals("1", createdUser.id)
//        assertEquals("test@example.com", createdUser.email)
//        assertEquals("password", createdUser.password)
//    }
//
//
//    @Test
//    fun testDeleteUser() {
//
//        `when`(userRepository.deleteById("1")).then { }
//
//        userService.deleteUser("1")
//
//        verify(userRepository, times(1)).deleteById("1")
//    }
//
//    @Test
//    fun testGetUserById() {
//        val user = User(
//            id = "1",
//            email = "test@example.com",
//            password = "password"
//        )
//
//        `when`(userRepository.findUser("1")).thenReturn(user)
//
//        val foundUser = userService.getUserById("1")
//
//        verify(userRepository, times(1)).findUser("1")
//
//        assertNotNull(foundUser)
//        assertEquals("1", foundUser.id)
//        assertEquals("test@example.com", foundUser.email)
//        assertEquals("password", foundUser.password)
//    }
//
//    @Test
//    fun testGetAllUsers() {
//        val userList = listOf(
//            User(
//                id = "1",
//                email = "test1@example.com",
//                password = "password1"
//            ),
//            User(
//                id = "2",
//                email = "test2@example.com",
//                password = "password2"
//            )
//        )
//
//        `when`(userRepository.findAll()).thenReturn(userList)
//
//        val allUsers = userService.getAllUsers()
//
//        verify(userRepository, times(1)).findAll()
//
//        assertNotNull(allUsers)
//        assertEquals(2, allUsers.size)
//    }
//}
