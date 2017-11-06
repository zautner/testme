package name.zautner.vitaly.appthis.app

import java.sql.ResultSet
import javax.inject.Inject

import org.springframework.jdbc.core.{JdbcTemplate, RowMapper}
import org.springframework.security.core.userdetails.{UserDetails, UserDetailsService}

/**
  * Customized implementation of[[UserDetailsService]],
  * which allows us to store a richer details object (with Long user ID)
  */
class OpaqueUrlUserDetailsService() extends UserDetailsService {
    final private val SQL_USER_BYNAME: String = "SELECT id, password FROM user WHERE account = ?"
    final private val rowMapper = new RowMapper[(java.lang.Long, String)] {
        override def mapRow(resultSet: ResultSet, i: Int): (java.lang.Long, String) = {
            (resultSet.getLong(1), resultSet.getString(2))
        }
    }
    @Inject
    var jdbcTemplate: JdbcTemplate = _

    /**
      * Here is the implementation itself, which loads user data from our DataSource.
      *
      * @param username Account
      * @return Modified [[UserDetails]] object with Long User ID
      */
    override def loadUserByUsername(username: String): UserDetails = {
        val userData: (java.lang.Long, String) = jdbcTemplate.queryForObject(SQL_USER_BYNAME, rowMapper, Array(username): _*)
        new OpaqueUrlUserDetails(username, userData._1, userData._2)
    }
}
