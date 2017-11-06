package name.zautner.vitaly.appthis.dao.impl

import name.zautner.vitaly.appthis.dao.model.User
import name.zautner.vitaly.appthis.dao.proto.UserRepository
import org.springframework.stereotype.Repository

/**
  * Implementation of [[UserRepository]] with [[AbstractRepository]]
  */
@Repository
class UserRepositoryHibernate extends AbstractRepository[User](classOf[User]) with UserRepository {

    override def findByName(name: String): Option[User] = {
        Option(getSession.createQuery("FROM User u WHERE u.name = :name")
          .setParameter("name", name)
          .uniqueResult().asInstanceOf[User])
    }
}
