package name.zautner.vitaly.appthis.svc.proto

import javax.security.auth.login.CredentialException

import name.zautner.vitaly.appthis.svc.Model.UserDTO
import org.springframework.security.core.Authentication

/**
  * Basic operation set for User
  */
trait UserService {
    def listUsers: scala.Iterable[UserDTO]

    @throws[IllegalAccessException]("Unknown user ID")
    def addUser(l: Long, account: String, password: String): UserDTO

    @throws[CredentialException]("No modification to holy account is permitted!")
    @throws[IllegalAccessException]("Unknown user ID")
    def modifyPassword(account: String, userId: Long, password: String, newPassword: String): Unit

    @throws[IllegalAccessException]("Unknown user ID")
    def deleteCurrentUser(l: Long): Unit

    def currentUserId(auth: Authentication): Long

    def currentUserAccount(auth: Authentication): String
}
