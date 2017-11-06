package name.zautner.vitaly.appthis.dao.proto

import java.io

/**
  * Abstract interface for Hibernate CRUD repository functionality
  *
  * @tparam E entity type
  * @author vitaly@clicksmob.com
  * @since 2015-07-11
  */
trait Repository[E] {
    def findById(id: Long): Option[E]

    def save(entity: E): io.Serializable

    def list(): Iterable[E]

    def delete(id: Long): Unit

    def delete(entity: E): Unit
}
