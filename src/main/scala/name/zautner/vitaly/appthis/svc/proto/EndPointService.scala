package name.zautner.vitaly.appthis.svc.proto

import name.zautner.vitaly.appthis.svc.Model.EndPointDTO

/**
  * Interface definition for the end-point service
  */
trait EndPointService {

    def listEndpointsForUser(userId: Long, urlPrefix: String = ""): scala.Iterable[EndPointDTO]

    @throws[IllegalArgumentException]("No user is found")
    def createEndPointForUser(url: String, userId: Long): String

    @throws[IllegalArgumentException]("No enity is found")
    def modifyEndPointForUser(alias: String, userId: Long, newUrl: String): String

    @throws[IllegalArgumentException]("No enity is found")
    def deleteEndPointForUser(alias: String, userId: Long): Unit

    def translateAlias(alias: String): String
}
