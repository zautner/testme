package name.zautner.vitaly.appthis.app

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

import scala.beans.BeanProperty

/**
  * Some extended [[org.springframework.security.core.userdetails.UserDetails]]
  * implementation which keeps also user's Long ID value.
  * It's going to serve housekeeping controllers, which need to deal with the current user.
  *
  * For the sake of rapid development
  * I also implement the most generic [[GrantedAuthority]] in the simple manner
  *
  * @param username     user account
  * @param userId       ID to store
  * @param userPassword Password to store for the further check
  */
class OpaqueUrlUserDetails(username: String, @BeanProperty val userId: Long, @BeanProperty userPassword: String)


  extends User(username: String, userPassword, java.util.Arrays.asList(new OpaqueUrlGrantedAuthority())) {

}

class OpaqueUrlGrantedAuthority extends GrantedAuthority {
    override def getAuthority = "SUPERADMIN"
}