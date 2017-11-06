package name.zautner.vitaly.appthis.dao.proto

import name.zautner.vitaly.appthis.dao.model.User


/**
  * Interface declaration for the User repository specific functionality
  */
trait UserRepository extends Repository[User] {
    def findByName(name: String): Option[User]
}
