package name.zautner.vitaly.appthis.svc.impl

import javax.inject.Inject
import javax.security.auth.login.CredentialException

import name.zautner.vitaly.appthis.app.OpaqueUrlUserDetails
import name.zautner.vitaly.appthis.dao.model.User
import name.zautner.vitaly.appthis.dao.proto.UserRepository
import name.zautner.vitaly.appthis.svc.Model.UserDTO
import name.zautner.vitaly.appthis.svc.proto.UserService
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl @Inject()(userDao: UserRepository,
                                endPointService: EndPointServiceImpl,
                                passwordEncoder: PasswordEncoder) extends UserService {
    private val logger = LoggerFactory.getLogger(getClass)

    @Transactional(readOnly = true)
    override def listUsers: Iterable[UserDTO] = userDao.list().map((user: User) => {
        logger.info(s"listUsers started")
        UserDTO(user.getAccount, endPointService.listEndpointsForUser(user.getId, ""))
    })

    @throws[IllegalAccessException]("Unknown user ID")
    @Transactional(readOnly = false)
    override def addUser(currentUserId: Long, account: String, password: String): UserDTO = {
        userDao.findById(currentUserId).getOrElse(throw new IllegalAccessException("Unknown user ID"))
        val user = new User
        user.setAccount(account)
        user.setPassword(password)
        userDao.save(user)
        UserDTO(account, List.empty)
    }

    @throws[CredentialException]("Holy account!")
    @throws[IllegalAccessException]("Unknown user ID")
    @Transactional(readOnly = false)
    override def modifyPassword(account: String, userId: Long, password: String, newPassword: String): Unit = {
        if (account.equalsIgnoreCase("admin")) throw new CredentialException(s"Holy account $account cannot be modified")
        val user: User = userDao.findById(userId).getOrElse(throw new IllegalAccessException("Unknown user ID"))
        user.setPassword(passwordEncoder.encode(newPassword))
    }

    @throws[IllegalAccessException]("Unknown user ID")
    @Transactional(readOnly = false)
    override def deleteCurrentUser(currentUserId: Long): Unit = {
        userDao.delete(userDao.findById(currentUserId).getOrElse(throw new IllegalAccessException("Unknown user ID")))
        SecurityContextHolder.getContext.setAuthentication(null)
    }

    override def currentUserId(auth: Authentication): Long = {
        val userDetails: OpaqueUrlUserDetails = classOf[OpaqueUrlUserDetails].cast(auth.getPrincipal)
        val userId: Long = userDetails.getUserId
        userId
    }

    override def currentUserAccount(auth: Authentication): String = {
        val userDetails: OpaqueUrlUserDetails = classOf[OpaqueUrlUserDetails].cast(auth.getPrincipal)
        userDetails.getUsername
    }
}
