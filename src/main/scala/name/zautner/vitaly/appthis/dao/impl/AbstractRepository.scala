package name.zautner.vitaly.appthis.dao.impl

import java.io
import javax.inject.Inject

import name.zautner.vitaly.appthis.dao.proto.Repository
import org.hibernate.{HibernateException, Session, SessionFactory}

import scala.collection.JavaConverters._

/**
  * Generic implementation of Hibernate CRUD repository functionality
  *
  * @param entityType We want to store the explicit entity class type
  *                   to use it in runtime
  * @tparam E entity type
  * @author vitaly@clicksmob.com
  * @since 2015-07-11
  */
abstract class AbstractRepository[E](entityType: Class[E]) extends Repository[E] {

    /**
      * Hibernate session factory
      */
    @Inject var sessionFactory: SessionFactory = _

    /**
      * List them all
      *
      * @return
      */

    override def list(): Iterable[E] = {
        getSession.createQuery(s"FROM ${entityType.getName}").list().asScala.map(_.asInstanceOf[E])
    }

    /**
      * Save it
      *
      * @param entity
      * @return
      */
    override def save(entity: E): io.Serializable = {
        Option(entity) match {
            case Some(_) => getSession.save(entity)
        }
    }

    /**
      * Delete it
      *
      * @param id
      */
    override def delete(id: Long): Unit = {
        val entity = findById(id)
        entity match {
            case Some(_) => delete(entity.get)
            case None => throw new IllegalArgumentException("No entity to delete!")
        }
    }

    /**
      * Find by id
      *
      * @param id
      * @return
      */
    override def findById(id: Long): Option[E] = {
        Option(id) match {
            case Some(_) => Option(getSession.get(entityType, id))
            case None => None
        }
    }

    /**
      * Delete it
      *
      * @param entity
      */
    override def delete(entity: E): Unit = getSession.delete(entity)

    /**
      * Standard hibernate session retrival sequence
      *
      * @return
      */
    protected def getSession: Session = {
        try {
            sessionFactory.getCurrentSession
        } catch {
            case he: HibernateException => sessionFactory.openSession()
        }
    }
}
