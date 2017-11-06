package name.zautner.vitaly.appthis.dao.proto

import name.zautner.vitaly.appthis.dao.model.EndPoint

/**
  * Interface declaration for the end-point repository specific functionality
  */
trait EndPointRepository extends Repository[EndPoint] {
    def findEntryPointByUrl(url: String): Option[EndPoint]

    def listForUser(userId: Long): Iterable[EndPoint]
}
