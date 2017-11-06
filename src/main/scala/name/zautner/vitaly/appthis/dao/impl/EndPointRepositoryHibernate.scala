package name.zautner.vitaly.appthis.dao.impl

import name.zautner.vitaly.appthis.dao.model.EndPoint
import name.zautner.vitaly.appthis.dao.proto.EndPointRepository
import org.hibernate.criterion.Restrictions
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._

/**
  * EndPoint repository functionality
  * Implements [[EndPointRepository]] interface with [[AbstractRepository]] functionality
  */

@Repository
class EndPointRepositoryHibernate() extends AbstractRepository[EndPoint](classOf[EndPoint]) with EndPointRepository {
    override def listForUser(userId: Long): Iterable[EndPoint] = {
        getSession.createCriteria(classOf[EndPoint])
          .createAlias("users", "u")
          .add(Restrictions.eq("u.id", userId))
          .list()
          .asScala.map(_.asInstanceOf[EndPoint])
    }


    override def findEntryPointByUrl(url: String): Option[EndPoint] = {
        Option(getSession.createCriteria(classOf[EndPoint])
          .add(Restrictions.eq("url", url))
          .uniqueResult()
          .asInstanceOf[EndPoint])
    }
}
